package com.asiainfo.dbcp.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:12:30
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Repository
public class UserDaoImpl implements IUserDao {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public User queryByUserId(int userId) {
		
		String sql = "select user_id, user_name from user where user_id=?";
		logger.debug("sql={}", sql);
		return this.jdbcTemplate.queryForObject(sql, new Object[] {userId}, new BeanPropertyRowMapper<User>(User.class));
	}

	@Override
	public void save(User user) {

		String sql = "insert into user(user_id, user_name) values(?, ?)";
		logger.debug("sql={}", sql);
		this.jdbcTemplate.update(sql, new Object[] {user.getUserId(), user.getUserName()});
	}
}
