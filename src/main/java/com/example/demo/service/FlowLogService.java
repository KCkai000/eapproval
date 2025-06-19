package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Flow;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;

@Service
public interface FlowLogService {
	
	void createFlowLog(LeaveForm leaveForm, Flow flow, User user);
}
