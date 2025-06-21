package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.exception.FlowException;
import com.example.demo.model.entity.Flow;
import com.example.demo.repository.FlowRepository;
import com.example.demo.service.FlowService;

@Service
public class FlowServiceImpl implements FlowService{
	
	@Autowired
	private FlowRepository flowRepository;
		
	@Override
	public Flow findNextFlow(Goto currentgoTo, State state, Action action) {
		// 用 currentgoTo 和 state 找所有可能的流程
		List<Flow> flows = flowRepository.findByCurrentGoToAndState(currentgoTo, state);
		if (flows == null || flows.isEmpty()) {
			return null; // 没有找到符合条件的流程
		}
		return flows.stream()
				.filter(f -> f.getAction() == action)
				.findFirst()
				.orElseThrow(() -> new FlowException("找不到符合條件的下個流程"));
	}

	@Override
	public Flow findInitialFlow(State state, Action action, Integer roleId) {
		return flowRepository.findByStateAndActionAndRoleId(state, action, roleId)
				.orElseThrow(() -> new FlowException("找不到符合條件的初始流程" + state + "+" + action));
	}

	@Override
	public Flow findNextFlowWithRole(Goto currentgoTo, State state, Action action, Integer roleId) {
		System.out.println("🔍 查詢流程條件：");
	    System.out.println("  - goTo: " + currentgoTo);
	    System.out.println("  - state: " + state);
	    System.out.println("  - action: " + action);
	    System.out.println("  - roleId: " + roleId);
		
		return flowRepository.findByCurrentgoToAndStateAndActionAndRole_Id(currentgoTo, state, action, roleId)
				.orElseThrow(()-> new FlowException("找不到符合條件的下個流程"));
	}

}
