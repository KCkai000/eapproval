package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequest {
	
	
	private String account; //帳號可以輸入信箱或帳號
	private String password; //密碼
	
	public String getAccountOrEmail() {
		return account;
	}
	
	
}
