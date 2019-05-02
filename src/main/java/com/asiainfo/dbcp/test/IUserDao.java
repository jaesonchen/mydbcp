package com.asiainfo.dbcp.test;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:10:10
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IUserDao {

	public User queryByUserId(int userId);
	
	public void save(User user);
}
