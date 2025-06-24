package com.example.demo.util;

import org.springframework.stereotype.Component;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.exception.FlowException;

@Component  // 用來處理審核流程
public class FlowActionResolver {
	
	public Action resolverNextAction(Goto currentGoto, Action userAction) {
		
		if(Goto.REVIEW_MANAGER.equals(currentGoto)) {
			switch(userAction){
			case APPROVED:
				return Action.PENDING;
			case REJECTED:
				return Action.REJECTED;
			default:
				throw new FlowException("manager不支援當前審核狀態" + userAction);
			
			}
		}else if(Goto.REVIEW_HR.equals(currentGoto)) {
			switch(userAction) {
			case APPROVED:
				return Action.APPROVED;
			case REJECTED:
				return Action.REJECTED;
			default:
				throw new FlowException("HR不支援當前審核狀態" + userAction);
			}
		}else {
			throw new FlowException("未知流程節點" + currentGoto);
		}		
		
	}


}
