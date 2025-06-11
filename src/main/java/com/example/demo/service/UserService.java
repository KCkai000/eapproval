package com.example.demo.service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;

public interface UserService {
	
 
	
	public User getUser(String username);	
	public void addUser(String username, String email, String account, String password, Boolean active, String role);
	public void updateUser(Integer id, String username, String email, Boolean active, String role);
	public void updateActive(Integer id,  Boolean active);			
	public void updateRole(Integer id, String role);
	public void deleteUser(Integer id);
		
		
}
