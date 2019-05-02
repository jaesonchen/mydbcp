package com.asiainfo.dbcp.factory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:  连接工厂，用于创建真正的数据库连接
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午8:58:26
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public interface ConnectionFactory {

    /**
     * Create a new {@link java.sql.Connection} in an implementation specific fashion.
     *
     * @return a new {@link java.sql.Connection}
     * @throws SQLException if a database error occurs creating the connection
     */
    public abstract Connection createConnection() throws SQLException;
}
