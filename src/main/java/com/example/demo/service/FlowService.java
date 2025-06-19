package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.model.entity.Flow;

@Service
public interface FlowService {
	
	Flow findNextFlow(Goto goTo, State state, Action action);
}
