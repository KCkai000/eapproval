package com.example.demo.detail;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.model.entity.User;

public class UserAuthDetails implements UserDetails{
	
	private User user; // 我自己的User Entity
	
	public UserAuthDetails(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		System.out.println("目前使用者角色：" + user.getRole().getName());
		return List.of(new SimpleGrantedAuthority("ROLE_" +user.getRole().getName()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getAccount();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true; // 帳號不過期
	}
	@Override
	public boolean isAccountNonLocked() {
		return true; // 帳號未被鎖定
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true; // 帳號驗證尚未過期
	}
	@Override
	public boolean isEnabled() {
		return true; // 帳號未被停用
	}
	
	public User getUser() {
		return user;
	}
}
