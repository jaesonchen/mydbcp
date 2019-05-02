package com.asiainfo.dbcp.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description: DriverManager实现的连接工厂
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午9:01:41
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DriverManagerConnectionFactory implements ConnectionFactory {

    protected String connectUri = null;
    protected String username = null;
    protected String password = null;
    protected Properties props = null;
    
    static {
        // Related to DBCP-212
        // Driver manager does not sync loading of drivers that use the service
        // provider interface. This will cause issues is multi-threaded environments. 
        // This hack makes sure the drivers are loaded before DBCP tries to use them.
        DriverManager.getDrivers();
    }
    
    public DriverManagerConnectionFactory(String connectUri, Properties props) {
        this.connectUri = connectUri;
        this.props = props;
    }

    public DriverManagerConnectionFactory(String connectUri, String username, String password) {
        this.connectUri = connectUri;
        this.username = username;
        this.password = password;
    }

	@Override
	public Connection createConnection() throws SQLException {
	    Connection conn = null;
        if (null == props) {
            if ((username == null) && (password == null)) {
                conn = DriverManager.getConnection(connectUri);
            } else {
                conn = DriverManager.getConnection(connectUri, username, password);
            }
        } else {
            conn = DriverManager.getConnection(connectUri, props);
        }
        return conn;
	}

    @Override
    public String toString() {
        return "DriverManagerConnectionFactory [connectUri=" + connectUri + ", username=" + username + ", password="
                + password + ", props=" + props + "]";
    }
}
