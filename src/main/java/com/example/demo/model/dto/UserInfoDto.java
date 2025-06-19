package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
	
	private Integer id;
	private String username; // 使用者名稱
	private String email; // 使用者電子郵件
	private String Role; //使用者職稱
	
}
