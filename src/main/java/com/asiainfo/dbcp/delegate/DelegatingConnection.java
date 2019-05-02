package com.asiainfo.dbcp.delegate;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.ClientInfoStatus;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 连接代理，持有真正的数据库连接，用于跟踪statement和resultset，在连接关闭时，会调用passivate方法，关闭Trace缓存的Statement；
 *             - 如果不需要跟踪Statement，则可以直接返回原始的Statement，也不需要继承AbandonedTrace。
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午9:13:58
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DelegatingConnection extends AbandonedTrace implements Connection {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	// 真正的数据库连接
	protected Connection conn = null;
	// 连接是否关闭
	protected boolean closed = false;
	
	public DelegatingConnection(Connection conn) {
		logger.debug("new DelegatingConnection() ......");
        this.conn = conn;
    }

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(getClass())) {
            return iface.cast(this);
        } else if (iface.isAssignableFrom(conn.getClass())) {
            return iface.cast(conn);
        } else {
            return conn.unwrap(iface);
        }
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return iface.isAssignableFrom(getClass()) || conn.isWrapperFor(iface);
	}

	@Override
	public Statement createStatement() throws SQLException {
		checkOpen();
        try {
        	logger.debug("createStatement() ......");
        	return new DelegatingStatement(this, conn.createStatement());
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={} ......", sql);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {

        checkOpen();
        try {
        	logger.debug("prepareCall(), sql={} ......", sql);
            return new DelegatingCallableStatement(this, conn.prepareCall(sql));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public String nativeSQL(String sql) throws SQLException {
		checkOpen();
		try {
		    logger.debug("nativeSQL(), sql={} ......", sql);
			return conn.nativeSQL(sql);
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		checkOpen();
		try {
			logger.debug("setAutoCommit(), autoCommit={} ......", autoCommit);
			conn.setAutoCommit(autoCommit);
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		checkOpen();
		try {
			return conn.getAutoCommit();
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public void commit() throws SQLException {
		checkOpen();
		try {
			logger.debug("commit() ......");
			conn.commit();
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public void rollback() throws SQLException {
		checkOpen(); 
		try {
			logger.debug("rollback() ......");
			conn.rollback();
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public void close() throws SQLException {
		logger.debug("close() ......");
		closed = true;
		// 连接关闭时，清理连接缓存的Statemment trace
		passivate();
		// 关闭连接，这里是真正的关闭，Pooling对象的关闭是返还连接池
		conn.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closed || conn.isClosed();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
        checkOpen();
        try {
            return conn.getMetaData();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
        checkOpen();
        try {
        	logger.debug("setReadOnly(), readOnly={} ......", readOnly);
        	conn.setReadOnly(readOnly);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		checkOpen();
		try {
			return conn.isReadOnly();
		} catch (SQLException e) {
			handleException(e);
			throw e;
		}
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		checkOpen();
        try {
        	logger.debug("setCatalog(), catalog={} ......", catalog);
        	conn.setCatalog(catalog);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public String getCatalog() throws SQLException {
        checkOpen();
        try {
            return conn.getCatalog();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		checkOpen();
        try {
        	logger.debug("setTransactionIsolation(), level={} ......", level);
        	conn.setTransactionIsolation(level);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
        checkOpen();
        try {
            return conn.getTransactionIsolation();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
        checkOpen();
        try {
            return conn.getWarnings();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void clearWarnings() throws SQLException {
		checkOpen();
        try {
        	logger.debug("clearWarnings() ......");
        	conn.clearWarnings();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
        	logger.debug("createStatement(), resultSetType={}, resultSetConcurrency={} ......", 
        	        resultSetType, resultSetConcurrency);
        	return new DelegatingStatement(this, conn.createStatement(resultSetType, resultSetConcurrency));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={}, resultSetType={}, resultSetConcurrency={} ......", 
        	        sql, resultSetType, resultSetConcurrency);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql, resultSetType, resultSetConcurrency));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareCall(), sql={}, resultSetType={}, resultSetConcurrency={} ......", 
        	        sql, resultSetType, resultSetConcurrency);
            return new DelegatingCallableStatement(this, conn.prepareCall(sql, resultSetType,resultSetConcurrency));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkOpen();
        try {
            return conn.getTypeMap();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		checkOpen();
        try {
        	logger.debug("setTypeMap(), map={} ......", map);
        	conn.setTypeMap(map);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		checkOpen();
        try {
        	conn.setHoldability(holdability);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public int getHoldability() throws SQLException {
        checkOpen();
        try {
            return conn.getHoldability();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}
	
	@Override
	public Savepoint setSavepoint() throws SQLException {
        checkOpen();
        try {
        	logger.debug("setSavepoint() ......");
            return conn.setSavepoint();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
        checkOpen();
        try {
        	logger.debug("setSavepoint(), name={} ......", name);
            return conn.setSavepoint(name);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		checkOpen();
        try {
        	logger.debug("rollback(), savepoint={} ......", savepoint);
        	conn.rollback(savepoint);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		checkOpen();
        try {
        	logger.debug("releaseSavepoint(), savepoint={} ......", savepoint);
        	conn.releaseSavepoint(savepoint);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, 
	        int resultSetHoldability) throws SQLException {

        checkOpen();
        try {
        	logger.debug("createStatement(), resultSetType={}, resultSetConcurrency={}, resultSetHoldability={} ......", 
        			resultSetType, resultSetConcurrency, resultSetHoldability);
            return new DelegatingStatement(this, 
                    conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {

        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={}, resultSetType={}, resultSetConcurrency={}, resultSetHoldability={} ......", 
        			resultSetType, resultSetConcurrency, resultSetHoldability);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql, 
        	        resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {

        checkOpen();
        try {
        	logger.debug("prepareCall(), sql={}, resultSetType={}, resultSetConcurrency={}, resultSetHoldability={} ......", 
        			resultSetType, resultSetConcurrency, resultSetHoldability);
        	return new DelegatingCallableStatement(this, conn.prepareCall(sql, 
        	        resultSetType, resultSetConcurrency, resultSetHoldability));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={}, autoGeneratedKeys={} ......", autoGeneratedKeys);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql, autoGeneratedKeys));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={}, columnIndexes={} ......", columnIndexes);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql, columnIndexes));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        checkOpen();
        try {
        	logger.debug("prepareStatement(), sql={}, columnNames={} ......", sql, columnNames);
        	return new DelegatingPreparedStatement(this, conn.prepareStatement(sql, columnNames));
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Clob createClob() throws SQLException {
        checkOpen();
        try {
        	logger.debug("createClob() ......");
            return conn.createClob();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Blob createBlob() throws SQLException {

        checkOpen();
        try {
        	logger.debug("createBlob() ......");
            return conn.createBlob();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public NClob createNClob() throws SQLException {
        checkOpen();
        try {
            logger.debug("createNClob() ......");
            return conn.createNClob();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
        checkOpen();
        try {
            logger.debug("createSQLXML() ......");
            return conn.createSQLXML();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
        checkOpen();
        try {
            return conn.isValid(timeout);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            checkOpen();
            logger.debug("setClientInfo(), name={}, value={} ......", name, value);
            conn.setClientInfo(name, value);
        } catch (SQLClientInfoException e) {
            handleException(e);
            throw e;
        } catch (SQLException e) {
            throw new SQLClientInfoException("Connection is closed.", 
                    Collections.<String, ClientInfoStatus>emptyMap(), e);
        }
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            checkOpen();
            logger.debug("setClientInfo(), properties={} ......", properties);
            conn.setClientInfo(properties);
        } catch (SQLClientInfoException e) {
            handleException(e);
            throw e;
        } catch (SQLException e) {
            throw new SQLClientInfoException("Connection is closed.", 
                    Collections.<String, ClientInfoStatus>emptyMap(), e);
        }
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
        checkOpen();
        try {
            return conn.getClientInfo(name);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Properties getClientInfo() throws SQLException {
        checkOpen();
        try {
            return conn.getClientInfo();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        checkOpen();
        try {
            logger.debug("createArrayOf, typeName={} ......", typeName);
            return conn.createArrayOf(typeName, elements);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        checkOpen();
        try {
            logger.debug("createStruct, typeName={} ......", typeName);
            return conn.createStruct(typeName, attributes);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setSchema(String schema) throws SQLException {
        checkOpen();
        try {
        	logger.debug("setSchema(), schema={} ......", schema);
            conn.setSchema(schema);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public String getSchema() throws SQLException {
        checkOpen();
        try {
            return conn.getSchema();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void abort(Executor executor) throws SQLException {
        checkOpen();
        try {
            logger.debug("abort ......");
            conn.abort(executor);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        checkOpen();
        try {
        	logger.debug("setNetworkTimeout(), executor={}, milliseconds={} ......", executor, milliseconds);
            conn.setNetworkTimeout(executor, milliseconds);
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
        checkOpen();
        try {
            return conn.getNetworkTimeout();
        } catch (SQLException e) {
            handleException(e);
            throw e;
        }
	}

	/**
	 * @Description: 处理sql异常，通常用于记录sql异常日志
	 * @author chenzq
	 * @date 2019年5月1日 下午9:21:33
	 * @param e
	 * @throws SQLException
	 */
	protected void handleException(SQLException e) {
		logger.debug("handleException(), SQLException={} ......", e);
    }
	
	/**
     * @Description: 检查持有的数据库连接是否已关闭
	 * @author chenzq
	 * @date 2019年5月1日 下午9:20:02
	 * @throws SQLException
	 */
	protected void checkOpen() throws SQLException {
        if (closed) {
            if (null != conn) {
                String label = "";
                try {
                    label = conn.toString();
                } catch (Exception ex) {
                    // ignore, leave label empty
                }
                throw new SQLException("Connection " + label + " is closed.");
            } else {
                throw new SQLException("Connection is null.");
            }      
        }
    }
	
	/**
	 * @Description: 连接新建或者激活时，调用该方法，
	 * @author chenzq
	 * @date 2019年5月1日 下午9:36:22
	 */
    public void activate() {
    	logger.debug("activate() ......");
        closed = false;
        setLastUsed();
        // 递归调用Delegating对象的activate
        if (conn instanceof DelegatingConnection) {
            ((DelegatingConnection) conn).activate();
        }
    }
    
    /**
     * @Description: 连接失效或者关闭时，调用该方法，以清理连接缓存的Statement trace
     * @author chenzq
     * @date 2019年5月1日 下午9:30:19
     * @throws SQLException
     */
    public void passivate() throws SQLException {
    	logger.debug("passivate() ......");
        // The JDBC spec requires that a Connection close any open Statement's when it is closed.
        // DBCP-288. Not all the traced objects will be statements
        List<AbandonedTrace> traceList = getTrace();
        if (traceList != null) {
            for (AbandonedTrace trace : traceList) {
                if (trace instanceof Statement) {
                	// 关闭对应的statement
                    ((Statement) trace).close();
                } else if (trace instanceof ResultSet) {
                    // DBCP-265: Need to close the result sets that are generated via DatabaseMetaData
                    // 关闭对应的ResultSet
                    ((ResultSet) trace).close();
                }
            }
            // 清空trace 记录
            clearTrace();
        }
        // 重置使用时间记录
        setLastUsed(0);
        // 如果连接还有一层代理，继续调用代理的passivate
        if(conn instanceof DelegatingConnection) {
            ((DelegatingConnection) conn).passivate();
        }
    }
	
    /**
     * @Description: 返回代理的对象
     * @author chenzq
     * @date 2019年5月1日 下午9:34:04
     * @return
     */
    protected final Connection getDelegateConnection() {
        Connection c = conn;
        while (c != null && c instanceof DelegatingConnection) {
            c = ((DelegatingConnection) c).getDelegateConnection();
            // 循环引用
            if (this == c) {
                return null;
            }
        }
        return c;
    }
    
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Connection delegate = getDelegateConnection();
        if (delegate == null) {
            return false;
        }
        if (obj instanceof DelegatingConnection) {
            DelegatingConnection c = (DelegatingConnection) obj;
            return delegate.equals(c);
        } else {
            return delegate.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        Object obj = getDelegateConnection();
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }
    
	@Override
    public String toString() {
        String str = null;
        Connection c = getDelegateConnection();
        if (c != null) {
            try {
                if (c.isClosed()) {
                    str = "connection is closed";
                } else {
                    DatabaseMetaData meta = c.getMetaData();
                    if (meta != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(meta.getURL());
                        sb.append(", UserName=");
                        sb.append(meta.getUserName());
                        sb.append(", ");
                        sb.append(meta.getDriverName());
                        str = sb.toString();
                    }
                }
            } catch (SQLException ex) {
                // Ignore
            }
        }
        
        if (str == null) {
            str = super.toString();
        }
        return str;
    }
}
