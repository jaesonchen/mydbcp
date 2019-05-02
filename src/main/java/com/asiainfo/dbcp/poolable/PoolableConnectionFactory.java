package com.asiainfo.dbcp.poolable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.asiainfo.dbcp.factory.ConnectionFactory;

/**
 * @Description: poolable的连接对象工厂，通过持有真正的连接工厂和对象池，实现连接池的makeObject、activateObject、validateObject、passivateObject、destroyObject
 * 
 * @author chenzq  
 * @date 2019年5月2日 上午11:20:16
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class PoolableConnectionFactory implements PooledObjectFactory<PoolableConnection> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final int UNKNOWN_TRANSACTIONISOLATION = -1;
	// 连接池
	protected volatile GenericObjectPool<Connection> pool = null;
	// 真正的连接工厂
    protected volatile ConnectionFactory factory = null;
    // 校验查询sql
    protected volatile String validationQuery = null;
    // 校验查询超时时间
    protected volatile int validationQueryTimeout = -1;
    // 缺省catalog
    protected String defaultCatalog;
    // 缺省只读配置
    protected boolean defaultReadOnly = false;
    // 缺省自动提交配置
    protected boolean defaultAutoCommit = true;
    // 缺省事务隔离级别
    protected int defaultTransactionIsolation = UNKNOWN_TRANSACTIONISOLATION;
    
    public PoolableConnectionFactory(ConnectionFactory factory) {
        this(factory, null, false, true);
    }
    public PoolableConnectionFactory(ConnectionFactory factory, 
            String validationQuery, boolean defaultAutoCommit) {
        this(factory, validationQuery, -1, false, defaultAutoCommit, null, UNKNOWN_TRANSACTIONISOLATION);
    }
	public PoolableConnectionFactory(ConnectionFactory factory, 
    		String validationQuery, boolean defaultReadOnly, boolean defaultAutoCommit) {
        this(factory, validationQuery, -1, defaultReadOnly, defaultAutoCommit, null, UNKNOWN_TRANSACTIONISOLATION);
    }
	public PoolableConnectionFactory(ConnectionFactory factory, 
	        String validationQuery, int validationQueryTimeout, boolean defaultReadOnly, 
	        boolean defaultAutoCommit, String defaultCatalog, int defaultTransactionIsolation) {
	    this.factory = factory;
	    this.validationQuery = validationQuery;
	    this.validationQueryTimeout = validationQueryTimeout;
	    this.defaultReadOnly = defaultReadOnly;
	    this.defaultAutoCommit = defaultAutoCommit;
	    this.defaultCatalog = defaultCatalog;
	    this.defaultTransactionIsolation = defaultTransactionIsolation;
    }

    @Override
    public PooledObject<PoolableConnection> makeObject() throws Exception {
        logger.debug("makeObject() ......");
        Connection conn = factory.createConnection();
        if (conn == null) {
            throw new IllegalStateException("Connection factory returned null from createConnection");
        }
        // 返回封装过的代理对象
        return new DefaultPooledObject<>(new PoolableConnection(conn));
    }

    @Override
    public void destroyObject(PooledObject<PoolableConnection> p) throws Exception {
        logger.debug("destroyObject(), obj={} ......", p.getObject());
        PoolableConnection conn = p.getObject();
        // 调用代理的方法真正关闭连接
        conn.reallyClose();
    }

    @Override
    public boolean validateObject(PooledObject<PoolableConnection> p) {
        logger.debug("validateObject(), obj={} ......", p.getObject());
        PoolableConnection conn = p.getObject();
        try {
            if (conn.isClosed()) {
                logger.error("connection:{} is closed!", conn);
                return false;
            }
            if (StringUtils.isNotEmpty(validationQuery)) {
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    stmt = conn.createStatement();
                    if (validationQueryTimeout > 0) {
                        stmt.setQueryTimeout(validationQueryTimeout);
                    }
                    rs = stmt.executeQuery(validationQuery);
                    if (!rs.next()) {
                        throw new SQLException("validationQuery didn't return a row");
                    }
                } finally {
                    closeSilently(rs, stmt);
                }
            }
            return true;
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @Override
    public void activateObject(PooledObject<PoolableConnection> p) throws Exception {
        logger.debug("activateObject(), obj={} ......", p.getObject());
        PoolableConnection conn = p.getObject();
        if (conn.getAutoCommit() != defaultAutoCommit) {
            conn.setAutoCommit(defaultAutoCommit);
        }
        if ((defaultTransactionIsolation != UNKNOWN_TRANSACTIONISOLATION) && 
                (conn.getTransactionIsolation() != defaultTransactionIsolation)) {
            conn.setTransactionIsolation(defaultTransactionIsolation);
        }
        if (conn.isReadOnly() != defaultReadOnly) {
            conn.setReadOnly(defaultReadOnly);
        }
        if ((defaultCatalog != null) && (!defaultCatalog.equals(conn.getCatalog()))) {
            conn.setCatalog(defaultCatalog);
        }
        // 激活时调用代理对象的activate
        conn.activate();
    }

    @Override
    public void passivateObject(PooledObject<PoolableConnection> p) throws Exception {
        logger.debug("passivateObject(), obj={} ......", p.getObject());
        PoolableConnection conn = p.getObject();
        if (!conn.getAutoCommit() && !conn.isReadOnly()) {
            conn.rollback();
        }
        conn.clearWarnings();
        // 钝化时调用代理对象的passivate进行statement缓存清理
        conn.passivate();
    }
    
    //AutoCloseable关闭工具
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
