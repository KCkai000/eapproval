package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5137", allowCredentials = "true")
@RestController
@RequestMapping("/System")
public class LeaveController {
	
	@PostMapping("/leave")
	public ResponseEntity<String> submitLeave(@RequestBody LeaveRequest request) {
		System.out.println("收到請假資料：");
		System.out.println("起始日期：" + request.getStartDate());
		System.out.println("結束日期：" + request.getEndDate());
		System.out.println("類型：" + request.getType());
		System.out.println("原因：" + request.getReason());
		
		return ResponseEntity.ok("請假申請已提交成功");
	}
	
	static class LeaveRequest {
		private String startDate;
		private String endDate;
		private String type;
		private String reason;

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
		
	}
}
