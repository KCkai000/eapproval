package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

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
	
	// 給審核用的流程查詢
	@Override
	public Flow findNextFlowByGoTo(Goto previousGoTo, State state, Action action) {
		// 用 currentgoTo 和 state 找所有可能的流程
		List<Flow> flows = flowRepository.findByCurrentGoToAndState(previousGoTo, state);
		if (flows == null || flows.isEmpty()) {
			System.out.println("找不到任何下一步流程（currentGoTo=" + previousGoTo + ", state=" + state + ")");
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
		return flowRepository.findAll().stream()
			.filter(f -> f.getCurrentgoTo() == currentgoTo)
			.filter(f -> f.getState() == state)
			.filter(f -> f.getAction() == action)
			.filter(f -> f.getRole().equals(roleId))
			.findFirst()
			.orElse(null);
//		System.out.println("🔍 查詢流程條件：");
//	    System.out.println("  - goTo: " + currentgoTo);
//	    System.out.println("  - state: " + state);
//	    System.out.println("  - action: " + action);
//	    System.out.println("  - roleId: " + roleId);
//		
//		return flowRepository.findByCurrentgoToAndStateAndActionAndRole_Id(currentgoTo, state, action, roleId)
//				.orElseThrow(()-> new FlowException("找不到符合條件的下個流程"));
	}
	
	// 給送單流程用的
	@Override
	public Flow findNextFlowByCurrent(Goto current, State state, Action action) {
		List<Flow> flows = flowRepository.findByCurrentAndStateAndAction(current, state, action);
		if(flows == null || flows.isEmpty()) {
			throw new FlowException("找不到下一步流程（current=" + current + ", state=" + state + ", action=" + action + ")");
		}
		return flows.get(0);
	}

	@Override
	public Optional<Flow> findFinalStep(Goto currentgoTo, State state, Action action) {
		return flowRepository.findFinalStep(currentgoTo, state, action);
	}

}
