package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.model.dto.FlowDto;
import com.example.demo.model.entity.Flow;
@Component
public class FlowMapper {
	
	public FlowDto toDto(Flow flow) {
		FlowDto dto = new FlowDto();
		dto.setId(flow.getId());
		dto.setState(flow.getState().name());
		dto.setStateLabel(flow.getState().getDisplayName());
		dto.setCurrentgoTo(flow.getCurrentgoTo().name());	
		dto.setGoTo(flow.getGoTo() != null ? flow.getGoTo().name() :null);
		dto.setAction(flow.getAction().name());
		dto.setRoleId(flow.getRole() != null ? flow.getRole().getId() : null);
		return dto;		
	}
	
	public Flow toEntity(FlowDto dto) {
		Flow flow = new Flow();
		flow.setState(Enum.valueOf(State.class, dto.getState().toString()));
		flow.setCurrentgoTo(Enum.valueOf(Goto.class, dto.getCurrentgoTo().toString()));
		if(dto.getGoTo() != null) {
			flow.setGoTo(Enum.valueOf(Goto.class, dto.getGoTo().toString()));
		} 
		flow.setAction(Enum.valueOf(Action.class, dto.getAction().toString()));		
		
		return flow;
	}
}
