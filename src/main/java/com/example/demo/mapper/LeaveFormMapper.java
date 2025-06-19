package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.model.dto.FlowLogDto;
import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.LeaveForm;


@Component
public class LeaveFormMapper {
	private final FlowLogMapper flowLogMapper;
	
	public LeaveFormMapper(FlowLogMapper flowLogMapper) {
		this.flowLogMapper = flowLogMapper;
	}
	
	public LeaveFormDto toDto(LeaveForm form) {
		LeaveFormDto dto = new LeaveFormDto();
		dto.setId(form.getId());
		dto.setFlowState(form.getState().getState().name());
		dto.setAction(form.getAction().getAction().name());
		dto.setStartDate(form.getStartDate());
		dto.setEndDate(form.getEndDate());
		dto.setReason(form.getReason());
		List<FlowLogDto> logs = form.getFlowLogs().stream()
				.map(flowLogMapper::toDto)
				.collect(Collectors.toList());
		dto.setFlowLogs(logs);
		return dto;
	}
	
//	public LeaveForm toEntity(LeaveFormDto dto) {
//        LeaveForm entity = new LeaveForm();
//        entity.setId(dto.getId());
//
//        // 將 String 轉為 Enum（State 和 Action）
//        entity.setState(State.valueOf(dto.getFlowState())); // 假設 DTO 中為 "SICK_LEAVE" 這種 enum name
//        entity.setAction(Action.valueOf(dto.getAction()));
//
//        // 日期格式
//        entity.setStartDate(LocalDate.parse(dto.getStartDate()));
//        entity.setEndDate(LocalDate.parse(dto.getEndDate()));
//
//        entity.setReason(dto.getReason());
//
//        // 注意：flowLogs 通常不從這裡塞入，因為會透過流程控制創建
//        return entity;
//    }
}
