package com.asiainfo.dbcp.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.asiainfo.dbcp.factory.ConnectionFactory;
import com.asiainfo.dbcp.factory.DriverConnectionFactory;
import com.asiainfo.dbcp.poolable.PoolableConnection;
import com.asiainfo.dbcp.poolable.PoolableConnectionFactory;

/**
 * @Description: 数据库连接池简单实现，构建GenericObjectPool对象池、GenericObjectPoolConfig对象池配置、DriverConnectionFactory驱动连接工厂、
 *             - PooledObjectFactory池化的连接工厂。
 *             - 提供create() 方法返回池化数据源SimpleDataSource实现。
 * 
 * @author chenzq  
 * @date 2019年5月2日 下午4:55:24
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class SimpleDataSource implements DataSource {

    public static final int UNKNOWN_TRANSACTIONISOLATION = -1;
    
    // 对象池
    protected volatile GenericObjectPool<PoolableConnection> connectionPool = null;
    protected volatile boolean closed = false;
    // 对象构建工厂配置
    protected String driverClassName = null;
    protected String url = null;
    protected String username = null;
    protected String password = null;
    // 对象属性配置
    protected int transactionIsolation = UNKNOWN_TRANSACTIONISOLATION;
    protected boolean readOnly = false;
	protected boolean autoCommit = true;
	protected String catalog = null;
    // 对象校验配置
    protected String validationQuery = null;
    protected int validationQueryTimeout = -1;
	// 池参数配置
	protected int initialSize = 0;
	protected int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
	protected int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;
	protected int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;
	protected long maxWaitMillis = GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;
	protected boolean testOnBorrow = GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW;
	protected boolean testOnReturn = GenericObjectPoolConfig.DEFAULT_TEST_ON_RETURN;
	protected boolean testWhileIdle = GenericObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE;
	protected long timeBetweenEvictionRunsMillis = GenericObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;
	protected int numTestsPerEvictionRun = GenericObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;
	protected long minEvictableIdleTimeMillis = GenericObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;
	// 日志
	protected PrintWriter logWriter = new PrintWriter(System.out);
  
    @Override
    public Connection getConnection() throws SQLException {
        try {
            PoolableConnection conn = createConnectionPool().borrowObject();
            // 返回连接时，注入连接池，用于close时返还到池中
            conn.setPool(this.connectionPool);
            return conn;
        } catch (Exception e) {
            logWriter.println("error on getConnection: " + e);
            throw new SQLException("error on getConnection!", e);
        }
    }
    
	/**
	 * @Description: 双重检查初始化GenericObjectPool
	 * @author chenzq
	 * @date 2019年5月2日 下午6:07:51
	 * @return
	 * @throws SQLException
	 */
	protected GenericObjectPool<PoolableConnection> createConnectionPool() throws SQLException {
	    if (closed) {
            throw new SQLException("Data source is closed");
        }
	    if (connectionPool != null) {
            return connectionPool;
        }
	    synchronized (this) {
            if (connectionPool != null) {
                return connectionPool;
            }
    	    // 连接工厂
    	    ConnectionFactory connFactory = createConnectionFactory();
    	    // pooled 对象工厂
    	    PooledObjectFactory<PoolableConnection> factory = createPoolableConnectionFactory(connFactory);
    	    // PoolConfig
    	    GenericObjectPoolConfig<PoolableConnection> poolConfig = createPoolConfig();
    	    // 构建池对象
    	    GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<>(factory, poolConfig);
    	    // 初始化最小连接数
    	    try {
    	        for (int i = 0 ; i < initialSize ; i++) {
    	            pool.addObject();
    	        }
    	    } catch (Exception e) {
    	        throw new SQLException("Error preloading the connection pool", e);
    	    }
    	    connectionPool = pool;
	    }
	    return connectionPool;
	}

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
    }
    
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
    }
    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported by BasicDataSource");
    }
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("BasicDataSource is not a wrapper.");
    }
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
    
	/**
	 * @Description: 判断数据源是否关闭
	 * @author chenzq
	 * @date 2019年5月2日 下午2:37:29
	 * @return
	 */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @Description: 关闭数据源
     * @author chenzq
     * @date 2019年5月2日 下午2:38:36
     * @throws SQLException
     */
    public synchronized void close() throws SQLException {
        closed = true;
        GenericObjectPool<PoolableConnection> oldpool = connectionPool;
        connectionPool = null;
        try {
            if (oldpool != null) {
                oldpool.close();
            }
        } catch (Exception e) {
            throw new SQLException("Cannot close connection pool", e);
        }
    }
    
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDriverClassName() {
        return this.driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
        if (StringUtils.isNotEmpty(driverClassName)) {
            this.driverClassName = driverClassName;
        }
    }
    
    public int getTransactionIsolation() {
        return transactionIsolation;
    }
    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }
    public boolean isReadOnly() {
        return readOnly;
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    public boolean isAutoCommit() {
        return autoCommit;
    }
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
    public String getCatalog() {
        return catalog;
    }
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public boolean getTestOnBorrow() {
        return this.testOnBorrow;
    }
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
        if (connectionPool != null) {
            connectionPool.setTestOnBorrow(testOnBorrow);
        }
    }
    public boolean getTestOnReturn() {
        return this.testOnReturn;
    }
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
        if (connectionPool != null) {
            connectionPool.setTestOnReturn(testOnReturn);
        }
    }
    public boolean getTestWhileIdle() {
        return this.testWhileIdle;
    }
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
        if (connectionPool != null) {
            connectionPool.setTestWhileIdle(testWhileIdle);
        }
    }
    public int getInitialSize() {
        return this.initialSize;
    }
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }
    public int getMaxTotal() {
        return maxTotal;
    }
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        if (connectionPool != null) {
            connectionPool.setMaxTotal(maxTotal);
        }
    }
    public int getMaxIdle() {
        return this.maxIdle;
    }
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
        if (connectionPool != null) {
            connectionPool.setMaxIdle(maxIdle);
        }
    }
    public int getMinIdle() {
        return this.minIdle;
    }
    public void setMinIdle(int minIdle) {
       this.minIdle = minIdle;
       if (connectionPool != null) {
           connectionPool.setMinIdle(minIdle);
       }
    }
    public long getMaxWaitMillis() {
        return this.maxWaitMillis;
    }
    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
        if (connectionPool != null) {
            connectionPool.setMaxWaitMillis(maxWaitMillis);
        }
    }
    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        if (connectionPool != null) {
            connectionPool.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
    }
    public int getNumTestsPerEvictionRun() {
        return this.numTestsPerEvictionRun;
    }
    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        if (connectionPool != null) {
            connectionPool.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        }
    }
    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        if (connectionPool != null) {
            connectionPool.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
    }
    public String getValidationQuery() {
        return this.validationQuery;
    }
    public void setValidationQuery(String validationQuery) {
        if (StringUtils.isNotEmpty(validationQuery)) {
            this.validationQuery = validationQuery;
        }
    }
    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }
    public void setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    /**
     * @Description: 创建连接工厂
     * @author chenzq
     * @date 2019年5月2日 下午3:24:07
     * @return
     * @throws SQLException
     */
	protected ConnectionFactory createConnectionFactory() throws SQLException {
            
		// Load the JDBC driver class
		Class<?> driverFromCCL = null;
		if (StringUtils.isNotEmpty(driverClassName)) {
			try {
				try {
				    driverFromCCL = Class.forName(driverClassName);
				} catch (ClassNotFoundException cnfe) {
					driverFromCCL = Thread.currentThread().getContextClassLoader().loadClass(driverClassName);
				}
			} catch (Throwable t) {
                String message = "Cannot load JDBC driver class '" + driverClassName + "'";
                getLogWriter().println(message);
				throw new SQLException(message, t);
			}
		}

		// Create a JDBC driver instance
		Driver driver = null;
		try {
			if (driverFromCCL == null) {
				driver = DriverManager.getDriver(url);
			} else {
				// Usage of DriverManager is not possible, as it does not
				// respect the ContextClassLoader
				driver = (Driver) driverFromCCL.newInstance();
				if (!driver.acceptsURL(url)) {
					throw new SQLException("No suitable driver", "08001");
				}
			}
		} catch (Throwable t) {
			String message = "Cannot create JDBC driver of class '" +
					(driverClassName != null ? driverClassName : "") +
					"' for connect URL '" + url + "'";
			getLogWriter().println(message);
            throw new SQLException(message, t);
		}
		
		// Can't test without a validationQuery
		if (StringUtils.isEmpty(validationQuery)) {
			setTestOnBorrow(false);
			setTestOnReturn(false);
			setTestWhileIdle(false);
		}

		Properties props = new Properties();
		// Set up the driver connection factory we will use
		String user = username;
		if (user != null) {
		    props.put("user", user);
		} else {
			getLogWriter().println("DBCP DataSource configured without a 'username'");
		}
		String pwd = password;
		if (pwd != null) {
		    props.put("password", pwd);
		} else {
			getLogWriter().println("DBCP DataSource configured without a 'password'");
		}
		return new DriverConnectionFactory(driver, url, props);
	}

	/**
	 * @Description: 创建PooledObjectFactory
	 * @author chenzq
	 * @date 2019年5月2日 下午3:35:53
	 * @param connFactory
	 * @return
	 */
	protected PooledObjectFactory<PoolableConnection> createPoolableConnectionFactory(ConnectionFactory connFactory) {
	    
	    PooledObjectFactory<PoolableConnection> pooledFactory = new PoolableConnectionFactory(
	            connFactory, 
	            validationQuery, 
	            validationQueryTimeout, 
	            readOnly, 
	            autoCommit, 
	            catalog, 
	            transactionIsolation);
	    return pooledFactory;
	}
	
	/**
	 * @Description: 连接池配置
	 * @author chenzq
	 * @date 2019年5月2日 下午4:51:20
	 * @return
	 */
	protected GenericObjectPoolConfig<PoolableConnection> createPoolConfig() {
	    GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
	    config.setMaxTotal(maxTotal);
	    config.setMaxIdle(maxIdle);
	    config.setMinIdle(minIdle);
	    config.setMaxWaitMillis(maxWaitMillis);
	    config.setTestOnBorrow(testOnBorrow);
	    config.setTestOnReturn(testOnReturn);
	    config.setTestWhileIdle(testWhileIdle);
	    config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	    config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
	    config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return config;
	}
}
