package com.asiainfo.dbcp.factory;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Description: Driver实现的连接工厂，需要传入Driver、url、connectionProperties
 * 
 * @author chenzq  
 * @date 2019年5月1日 下午8:58:42
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved.
 */
public class DriverConnectionFactory implements ConnectionFactory {

    protected Driver driver = null;
    protected String connectUri = null;
    protected Properties props = null;
    
    public DriverConnectionFactory(Driver driver, String connectUri, Properties props) {
        this.driver = driver;
        this.connectUri = connectUri;
        this.props = props;
        assert null != this.driver && null != this.connectUri;
    }

	@Override
	public Connection createConnection() throws SQLException {
		return driver.connect(connectUri, props);
	}
	
	@Override
	public String toString() {
        return this.getClass().getName() + " [" + String.valueOf(driver) + ";" + String.valueOf(connectUri) + ";"  + String.valueOf(props) + "]";
    }
}
