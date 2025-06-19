package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.UserException;
import com.example.demo.model.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;	
	
	@Autowired
	private RoleRepository roleRepository;
		
	@Override
	@Transactional(readOnly = true)
	public List<User> findAllUsers() {
		return userRepository.findAll();		
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> searchUsers(String keyword) {
		return userRepository.searchByUsernameOrAccountOrEmail(keyword);
	}
	
	@Override
	@Transactional(readOnly = true)	
	public User findByAccount(String account) {
		return userRepository.findByAccount(account)
			.orElseThrow(() -> new UserException("找不到帳號: " + account));
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addUser(String username, String email, String account, String password, Boolean active, Integer role) throws UserException{
		String encoderPassword =passwordEncoder.encode(password);
		User user = new User();
		if(username == null || username.isBlank()) {
			throw new UserException("使用者名稱不能為空");
		}else if(!username.matches("[a-zA-Z\u4e00-\u9fa5]+")) {
			throw new UserException("使用者名稱只能包含中文和英文字母");
		}else if(username.matches(".*\\d.*")){
			throw new UserException("使用者名稱不能包含數字");
		}
		
		user.setUsername(username);
		if (email == null || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
	        throw new UserException("Email 格式不正確");
	    }
		
		user.setEmail(email);
		if (account == null || account.isBlank()) {
	        throw new UserException("帳號不能為空");
	    }
		user.setAccount(account);
		user.setPassword(encoderPassword);
		user.setActive(active);
		user.setRole(roleRepository.findById(role)
				.orElseThrow(() -> new UserException("找不到角色: " + role.getClass().getName())));
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserName(Integer id, String newUsername) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserException("找不到id為" + id + "的使用者，無法更新使用者"));
		if(newUsername == null || newUsername.isBlank()) {
			throw new UserException("使用者名稱不能為空");
		}else if(!newUsername.matches("[a-zA-Z\u4e00-\u9fa5]+")) {
			throw new UserException("使用者名稱只能包含中文和英文字母");
		}else if(!newUsername.matches(".*\\d.*")){
			throw new UserException("使用者名稱不能包含數字");
		}
		user.setUsername(newUsername);
		userRepository.save(user);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateUserEmail(Integer id, String newEmail) {
		User user = userRepository.findById(id)
					.orElseThrow(() -> new UserException("找不到id為" + id + "的使用者，無法更新使用者"));
		Optional<User> existingUserWithNewUserEmail = userRepository.findByAccount(newEmail);
		if(existingUserWithNewUserEmail.isPresent() && !existingUserWithNewUserEmail.get().getId().equals(id)) {
			throw new UserException("該信箱" + newEmail + "已存在");
		}
		user.setEmail(newEmail);
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateActive(Integer id, Boolean active) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserException("找不到id為" + id + "的使用者，無法更新使用者"));
		
		user.setActive(active);
		userRepository.save(user);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateRole(Integer id, String newRole) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserException("找不到id為" + id + "的使用者，無法更新使用者"));
		user.setRole(roleRepository.findByName(newRole)
				.orElseThrow(() -> new UserException("找不到職稱: " + newRole)));
		userRepository.save(user);	
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(Integer id) throws UserException {
		if(!userRepository.existsById(id)) {
			throw new UserException("找不到ID為" + id + "的使用者，無法刪除");
		}
		userRepository.deleteById(id);
	}

		

}
