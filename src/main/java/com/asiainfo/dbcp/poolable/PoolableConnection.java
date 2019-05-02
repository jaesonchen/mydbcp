package com.asiainfo.dbcp.poolable;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.asiainfo.dbcp.delegate.DelegatingConnection;

/**
 * @Description: Poolable连接，可以存放在对象池中的代理连接对象，需要一个对象池和被缓存的连接对象（通常是DelegatingConnection），
 *             - Poolable连接的close方法会将连接返还对象池中而不是真的关闭。
 * 
 * @author chenzq  
 * @date 2019年5月2日 上午10:56:00
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class PoolableConnection extends DelegatingConnection {

	protected GenericObjectPool<PoolableConnection> pool = null;
	
	public PoolableConnection(Connection conn) {
        super(conn);
    }
	
	/**
	 * @Description: 设置连接对应的连接池，用于close时返还连接池
	 * @author chenzq
	 * @date 2019年5月2日 下午5:25:25
	 * @param pool
	 */
	public void setPool(GenericObjectPool<PoolableConnection> pool) {
	    this.pool = pool;
	}
	
	@Override
    public synchronized void close() throws SQLException {
    	
        if (closed) {
            // already closed
            return;
        }

        boolean isUnderlyingConectionClosed;
        try {
            // 持有的连接对象是否关闭
            isUnderlyingConectionClosed = conn.isClosed();
        } catch (SQLException e) {
            // isClosed 出现异常，通常是连接失效
            try {
                // 调用对象池的invalidateObject处理失效的池对象，通常是destroy
                pool.invalidateObject(this);
            } catch (IllegalStateException ise) {
                // pool is closed, so close the connection
                // 连接池已关闭，清理代理的连接trace缓存，并关闭真正的连接
                passivate();
                getDelegateConnection().close();
            } catch (Exception ie) {
                // DO NOTHING the original exception will be rethrown
            }
            throw (SQLException) new SQLException("Cannot close connection (isClosed check failed)").initCause(e);
        }

        // 真正连接未关闭，返还当前的池连接到对象池中
        if (!isUnderlyingConectionClosed) {
            // Normal close: underlying connection is still open, 
            // so we simply need to return this proxy to the pool
            try {
                pool.returnObject(this);
            } catch(IllegalStateException e) {
                // pool is closed, so close the connection
                // 连接池已关闭，清理代理的连接trace缓存，并关闭真正的连接
                passivate();
                getDelegateConnection().close();
            } catch(RuntimeException e) {
                throw e;
            } catch(Exception e) {
                throw (SQLException) new SQLException("Cannot close connection (return to pool failed)").initCause(e);
            }
        // 真正连接已关闭
        } else {
            // Abnormal close: underlying connection closed unexpectedly, so we must destroy this proxy
            try {
                pool.invalidateObject(this);
            } catch(IllegalStateException e) {
                // pool is closed, so close the connection
                // 连接池已关闭，清理代理的连接trace缓存，并关闭真正的连接
                passivate();
                getDelegateConnection().close();
            } catch (Exception ie) {
                // DO NOTHING, "Already closed" exception thrown below
            }
            throw new SQLException("Already closed.");
        }
    }
	
	/**
	 * @Description: TODO
	 * @author chenzq
	 * @date 2019年5月2日 下午5:36:08
	 * @throws SQLException
	 */
	public void reallyClose() throws SQLException {
	    closed = true;
	    // 清理trace缓存
        passivate();
        // 调用代理对象的close
        getDelegateConnection().close();
	}
}
