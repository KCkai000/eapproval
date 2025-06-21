package com.example.demo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
	
	public static String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public static String getCurrentRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("尚未登入");
		}
		
		// 解析 ROLE_ 開頭的權限
		return authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.filter(auth -> auth.startsWith("ROLE_"))
				.map(auth -> auth.substring(5))  //去除 ROLE_
				.findFirst()
				.orElseThrow(()-> new RuntimeException("找不到角色"));
	}
}
