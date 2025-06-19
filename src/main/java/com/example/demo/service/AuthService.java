package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {
	
	/**
	 * 使用者登入
	 * @param accountOrEmail 帳號或信箱
	 * @param password 密碼
	 * @return 登入成功的使用者資訊
	 */
	String login(String accountOrEmail, String password);
	
	
	
}
