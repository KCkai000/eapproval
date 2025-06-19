package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.exception.AuthException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.util.JWTUtil;

@Service
public class AuthServiceImpl implements AuthService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public String login(String accountOrEmail, String password) throws AuthException {
		Optional<User> optUser = userRepository.findByAccount(accountOrEmail);
			if (optUser.isEmpty()) {
				optUser = userRepository.findByEmail(accountOrEmail);
			}
			
			optUser.orElseThrow(() -> new AuthException("帳號或信箱不存在"));
		    if (!passwordEncoder.matches(password, optUser.get().getPassword())) {
		        throw new AuthException("密碼錯誤");
		    }
		    
		    String token = jwtUtil.generateToken(optUser.get().getAccount());
		    System.out.println("輸入的是: " + accountOrEmail);
		    System.out.println("實際帳號是: " + optUser.get().getAccount());
		    System.out.println("Token 是: " + token);

		    return token;
	}
}
