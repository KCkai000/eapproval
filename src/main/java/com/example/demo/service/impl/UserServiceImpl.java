package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addUser(String username, String email, String account, String password, Boolean active, String role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(Integer id, String username, String email, Boolean active, String role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateActive(Integer id, Boolean active) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRole(Integer id, String role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(Integer id) {
		// TODO Auto-generated method stub
		
	}

}
