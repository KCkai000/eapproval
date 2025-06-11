package com.example.demo.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	
	private Integer id; // 使用者ID
	private String username;
	private String email;
	private String account;	
	private String password;
	private String role;
	private Boolean active = true; // 預設為建立即啟用
	
	
}
