package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.UserLoginController.LoginRequest;

@RestController
@RequestMapping("/System")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserLoginController {
	@PostMapping("/login")
	public ResponseEntity<Map<String,Object>> login(@RequestBody LoginRequest loginRequest) {
		Map<String, Object> response = new HashMap<>();
		if("admin".equals(loginRequest.getUsername()) && 
			"1234".equals(loginRequest.getPassword())) {
			
			response.put("message", "登入成功");
			response.put("userId", 1);
			return ResponseEntity.ok(response);
			
		} else {
			response.put("message", "登入失敗");
			return ResponseEntity.status(401).body(response);
		}
	}
	static class LoginRequest {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}
}
