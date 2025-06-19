package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.detail.UserAuthDetails;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserAuthDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =userRepository.findByAccount(username)
				.orElseThrow(() -> new UsernameNotFoundException("使用者不存在"+ username));
		return new UserAuthDetails(user);
	}

}
