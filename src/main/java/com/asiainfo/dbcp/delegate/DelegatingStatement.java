package com.asiainfo.dbcp.delegate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;

/**
 * @Description: Statement代理，构建时将Statement加入连接的Trace队列里
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午10:09:55
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DelegatingStatement extends AbandonedTrace implements Statement {

    // 真正的statement
	protected Statement stmt;
	// 创建statement的connection
	protected DelegatingConnection conn;
	
	public DelegatingStatement(DelegatingConnection conn, Statement stmt) {
	    this.conn = conn;
		this.stmt = stmt;
		// 添加当前statement到connection的trace里
		this.conn.addTrace(this);
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(getClass())) {
            return iface.cast(this);
        } else if (iface.isAssignableFrom(stmt.getClass())) {
            return iface.cast(stmt);
        } else {
            return stmt.unwrap(iface);
        }
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(getClass()) || stmt.isWrapperFor(iface);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
	    return new DelegatingResultSet(this, stmt.executeQuery(sql));
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
	    return stmt.executeUpdate(sql);
	}

	// 关闭时清理Result trace
	@Override
	public void close() throws SQLException {
        try {
            // 从connection trace里删除statement trace
            if (conn != null) {
                conn.removeTrace(this);
                conn = null;
            }
            // The JDBC spec requires that a statment close any open ResultSet's when it is closed.
            // FIXME The PreparedStatement we're wrapping should handle this for us.
            // See bug 17301 for what could happen when ResultSets are closed twice.
            List<AbandonedTrace> resultSets = getTrace();
            if( resultSets != null) {
                for (AbandonedTrace trace : resultSets) {
                    ((ResultSet) trace).close();;
                }
            }
            // 关闭持有的stmt
            stmt.close();
            stmt = null;
        } finally {
            if (null != stmt) {
                closeSilently(stmt);
            }
            conn = null;
            stmt = null;
            // 清空statement trace缓存
            clearTrace();
        }
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
	    return stmt.getMaxFieldSize();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
	    stmt.setMaxFieldSize(max);
	}

	@Override
	public int getMaxRows() throws SQLException {
	    return stmt.getMaxRows();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
	    stmt.setMaxRows(max);
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		stmt.setEscapeProcessing(enable);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return stmt.getQueryTimeout();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		stmt.setQueryTimeout(seconds);
	}

	@Override
	public void cancel() throws SQLException {
		stmt.cancel();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return stmt.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		stmt.clearWarnings();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		stmt.setCursorName(name);
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		return stmt.execute(sql);
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return new DelegatingResultSet(this, stmt.getResultSet());
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return stmt.getUpdateCount();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return stmt.getMoreResults();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		stmt.setFetchDirection(direction);
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return stmt.getFetchDirection();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		stmt.setFetchSize(rows);
	}

	@Override
	public int getFetchSize() throws SQLException {
		return stmt.getFetchSize();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return stmt.getResultSetConcurrency();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return stmt.getResultSetType();
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		stmt.addBatch(sql);
	}

	@Override
	public void clearBatch() throws SQLException {
		stmt.clearBatch();
	}

	@Override
	public int[] executeBatch() throws SQLException {
		return stmt.executeBatch();
	}

	@Override
	public Connection getConnection() throws SQLException {
		return conn;
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		return stmt.getMoreResults(current);
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return new DelegatingResultSet(this, stmt.getGeneratedKeys());
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		return stmt.executeUpdate(sql, autoGeneratedKeys);
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		return stmt.executeUpdate(sql, columnIndexes);
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		return stmt.executeUpdate(sql, columnNames);
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		return stmt.execute(sql, autoGeneratedKeys);
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return stmt.execute(sql, columnIndexes);
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		return stmt.execute(sql, columnNames);
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return stmt.getResultSetHoldability();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return conn.isClosed() || stmt.isClosed();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		stmt.setPoolable(poolable);
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return stmt.isPoolable();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		stmt.closeOnCompletion();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		return stmt.isCloseOnCompletion();
	}

	@Override
	public String toString() {
	    return stmt.toString();
	}
	
	/**
	 * @Description: AutoCloseable关闭工具
	 * @author chenzq
	 * @date 2019年5月2日 上午10:37:22
	 * @param autoCloseables
	 */
	protected void closeSilently(AutoCloseable... autoCloseables) {
	    if (null != autoCloseables) {
	        for (AutoCloseable closeable : autoCloseables) {
	            try {
	                closeable.close();
	            } catch(Exception ex) {
	                // ignore
	            }
	        }
	    }
	}
}