package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.LeaveFormException;
import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LeaveFormService;
import com.example.demo.util.JWTUtil;


@RestController
@RequestMapping("/leave")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class LeaveFormController {

		@Autowired
		private LeaveFormService leaveFormService;
		@Autowired
		private UserRepository userRepository;
		
		@PostMapping("/create")
		public ResponseEntity<LeaveForm> createLeaveForm(@RequestBody LeaveFormDto dto) {
			LeaveForm leaveForm = leaveFormService.createLeaveForm(dto);
			return ResponseEntity.ok(leaveForm);
		}
		
		private User getCurrentUser() {
		    String username = org.springframework.security.core.context.SecurityContextHolder
		            .getContext().getAuthentication().getName();
		    return userRepository.findByAccount(username)
		            .orElseThrow(() -> new RuntimeException("使用者不存在：" + username));
		}
		
		@GetMapping("/my-forms")
		public ResponseEntity<?> findLeaveFormDtos() {
			User currentUser = getCurrentUser();
			System.out.println("JWT 帳號：" + currentUser.getAccount());
			System.out.println("當前請求 ID：" + currentUser.getId());
			System.out.println("目前使用者 ID：" + currentUser.getId());
			return ResponseEntity.ok(leaveFormService.findLeaveFormDtos(currentUser.getId()));
		}
		
		@PutMapping("/update")
		public ResponseEntity<?> updateLeaveForm(@RequestBody LeaveForm leaveForm) {
		    try {
		        LeaveForm updated = leaveFormService.updateLeaveForm(leaveForm);
		        return ResponseEntity.ok(updated);
		    } catch (LeaveFormException e) {
		        return ResponseEntity.badRequest().body(e.getMessage());
		    }
		}

		
		@DeleteMapping("/deactivate/{id}")
		public ResponseEntity<?> deactivateLeaveForm(@PathVariable Integer id) {
			try {
				leaveFormService.deactivateLeaveForm(id);
				return ResponseEntity.ok("該假單已於頁面刪除");
			}catch (LeaveFormException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}
		
		@Autowired
		private JWTUtil jwtUtil;

		@GetMapping("/test-token")
		public ResponseEntity<String> testToken(@RequestParam String token) {
		    try {
		        String subject = jwtUtil.validateTokenAndRetrieveSubject(token);
		        return ResponseEntity.ok("解析出來的帳號：" + subject);
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.badRequest().body("Token 解析失敗：" + e.getMessage());
		    }
		}

}
