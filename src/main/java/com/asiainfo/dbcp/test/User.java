package com.asiainfo.dbcp.test;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:10:36
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int userId;
	private String userName;
	
	public User() {}
	public User(int userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + "]";
	}
}
