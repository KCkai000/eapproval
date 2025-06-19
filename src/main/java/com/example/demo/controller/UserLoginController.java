package com.example.demo.controller;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.AuthException;
import com.example.demo.model.dto.LoginRequest;
import com.example.demo.model.dto.LoginResponseDto;
import com.example.demo.model.dto.UserInfoDto;
import com.example.demo.model.entity.User;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/System")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserLoginController {
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			//回傳使用者資訊給前端，讓他可以顯示使用者在介面上
			User user = userService.findByAccount(request.getAccountOrEmail());			
	        String token = authService.login(request.getAccountOrEmail(), request.getPassword());
	        System.out.println("== token from service: " + token);
	        System.out.println("帳號：" + request.getAccount());
	        System.out.println("email/帳號登入欄位：" + request.getAccountOrEmail());

	        UserInfoDto userInfo = new UserInfoDto(
	        	user.getId(),
	        	user.getUsername(),
	        	user.getEmail(),
	        	user.getRole().getName()
	        );
	        System.out.println("Login user: " + user.getId() + " / " + user.getRole().getName());

	        LoginResponseDto response = new LoginResponseDto(token, userInfo);
	        return ResponseEntity.ok(response);
	        
	    } catch (AuthException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Map.of("error", e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "伺服器發生錯誤"));
	    }
	}

}
