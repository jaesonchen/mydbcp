package com.asiainfo.dbcp.test;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:19:19
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public interface IUserService {

	public User queryUserById(int userId);
	
	public void save(int userId, String userName);
}
