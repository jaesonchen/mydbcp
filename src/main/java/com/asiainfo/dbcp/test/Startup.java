package com.asiainfo.dbcp.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * 
 * @author       zq
 * @date         2017年5月10日  下午12:22:05
 * Copyright: 	  北京亚信智慧数据科技有限公司
 */
@Component
public class Startup {

	@Autowired
	private IUserService service;
	
	public static void main(String[] args) {
		
		try (ClassPathXmlApplicationContext context = 
		        new ClassPathXmlApplicationContext(new String[] {"classpath:application-db-configure.xml"})) {
		    
		    Startup startup = context.getBean(Startup.class);
	        startup.service.save(10004, "jaesonchen");
	        System.out.println(startup.service.queryUserById(10004));
		}
	}
}
