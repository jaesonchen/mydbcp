package com.asiainfo.dbcp.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:20:14
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserDao dao;
	
	@Override
	public User queryUserById(int userId) {
		logger.debug("userId={}", userId);
		return this.dao.queryByUserId(userId);
	}

	@Override
	public void save(int userId, String userName) {
		logger.debug("userId={}, userName={}", userId, userName);
		this.dao.save(new User(userId, userName));
		//throw new RuntimeException("after save");
	}
}
