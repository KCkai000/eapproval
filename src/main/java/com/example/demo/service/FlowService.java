package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.model.entity.Flow;

@Service
public interface FlowService {
	
	Flow findNextFlow(Goto currentgoTo, State state, Action action);
	Flow findInitialFlow(State state, Action action, Integer roleId);
	Flow findNextFlowWithRole(Goto currentgoTo, State state, Action action, Integer roleId);
}
