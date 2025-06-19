package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.exception.UserException;
import com.example.demo.model.entity.User;


@Service
public interface UserService {
	
	
	List<User> searchUsers(String keyword);
	List<User> findAllUsers();
	User findByAccount(String account);	
		
	public void addUser(String username, String email, String account, String password, Boolean active, Integer role_id) throws UserException;
	public void updateUserName(Integer id,  String username) throws UserException;
	public void updateUserEmail(Integer id, String email) throws UserException;
	public void updateActive(Integer id,  Boolean active) throws UserException;			
	public void updateRole(Integer id, String role) throws UserException;
	public void deleteUser(Integer id) throws UserException;
		
		
}
