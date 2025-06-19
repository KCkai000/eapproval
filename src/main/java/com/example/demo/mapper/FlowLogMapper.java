package com.example.demo.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.demo.model.dto.FlowLogDto;
import com.example.demo.model.entity.FlowLog;

@Component
public class FlowLogMapper {
	
	public FlowLogDto toDto(FlowLog log) {
		FlowLogDto dto = new FlowLogDto();
		dto.setId(log.getId());
		dto.setLeaveForm("LeaveForm#" + log.getLeaveForm().getId());
		dto.setFlow(log.getFlow().getAction().name());
		dto.setUser(log.getUser().getUsername() + " (" + log.getUser().getRole().getName() + ")");
		dto.setCreateTime(log.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		return dto;
	}
}
