package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
	
	private String token; // JWT token
	private UserInfoDto user; // 使用者ID
	
}