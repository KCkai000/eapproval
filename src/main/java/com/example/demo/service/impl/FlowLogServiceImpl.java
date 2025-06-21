package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.entity.Flow;
import com.example.demo.model.entity.FlowLog;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;
import com.example.demo.repository.FlowLogRepository;
import com.example.demo.service.FlowLogService;

@Service
public class FlowLogServiceImpl implements FlowLogService{
	
	@Autowired
	private FlowLogRepository flowLogRepository;
	
	@Override
	public void createFlowLog(LeaveForm leaveForm, Flow flow, User user) {
		FlowLog log = new FlowLog();
		log.setLeaveForm(leaveForm);
		log.setFlow(flow);
		log.setUser(user);
		
		flowLogRepository.save(log);
	}

}
