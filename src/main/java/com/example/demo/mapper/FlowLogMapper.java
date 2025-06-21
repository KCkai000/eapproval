package com.example.demo.mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.demo.model.dto.FlowLogDto;
import com.example.demo.model.entity.FlowLog;

import jakarta.transaction.Transactional;

@Component
public class FlowLogMapper {
	
	@Transactional
	public FlowLogDto toDto(FlowLog log) {
		FlowLogDto dto = new FlowLogDto();
		dto.setId(log.getId());
		dto.setLeaveForm("LeaveForm#" + log.getLeaveForm().getId());
		dto.setFlow(log.getFlow().getAction().name());
		if(log.getUser() != null) {
			 String roleName = log.getUser().getRole() != null ? log.getUser().getRole().getName() : "未知角色";
			    dto.setUser(log.getUser().getUsername() + " (" + roleName + ")");
			} else {
			    dto.setUser("未知使用者");
			}
		dto.setCreateTime(log.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		return dto;
	}
}
