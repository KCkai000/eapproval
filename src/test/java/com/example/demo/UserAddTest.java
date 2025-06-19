package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.UserService;


@SpringBootTest
public class UserAddTest {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void Test() {
		userService.addUser("Emily", "Emily@gmail.com", "w250617", "98765", true, 2);
		System.out.println("使用者建立成功d(^w^)b");
//		userService.addUser("Debby", "debby@gmail.com", "w250614", "98765", true, 1);
//		System.out.println("使用者建立成功d(^w^)b");
//		userService.addUser("Zev", "Zev@gmail.com", "w250615", "98765", true, 3);
//		System.out.println("使用者建立成功d(^w^)b");
	}
	

}
