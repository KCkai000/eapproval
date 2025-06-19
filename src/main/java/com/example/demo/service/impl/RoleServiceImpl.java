package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void addRole(String name) {
		Role role = new Role();
		role.setName(name);
		roleRepository.save(role);
		
	};
}
