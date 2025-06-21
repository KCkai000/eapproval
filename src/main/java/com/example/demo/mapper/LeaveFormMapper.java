package com.example.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.model.dto.FlowLogDto;
import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.Flow;
import com.example.demo.model.entity.LeaveForm;

import jakarta.transaction.Transactional;



@Component
public class LeaveFormMapper {
	private final FlowLogMapper flowLogMapper;
	
	public LeaveFormMapper(FlowLogMapper flowLogMapper) {
		this.flowLogMapper = flowLogMapper;
	}
	
	@Transactional
	public LeaveFormDto toDto(LeaveForm form) {
		LeaveFormDto dto = new LeaveFormDto();
		dto.setId(form.getId());
		dto.setFlowState(form.getCurrentFlow().getState().getDisplayName()); // 假設 State 有 getDisplayName 方法
		dto.setAction(form.getCurrentFlow().getAction().getDisplayName()); // 假設 Action 有 getDisplayName 方法
		dto.setStartDate(form.getStartDate());
		dto.setEndDate(form.getEndDate());
		dto.setReason(form.getReason());
		List<FlowLogDto> logs = form.getFlowLogs().stream()
				.map(flowLogMapper::toDto)
				.collect(Collectors.toList());
		dto.setFlowLogs(logs);
		return dto;
	}
	
	public LeaveForm toEntity(LeaveFormDto dto) {
        LeaveForm form = new LeaveForm();   

        // 日期格式
        form.setStartDate(dto.getStartDate());
        form.setEndDate((dto.getEndDate()));
        form.setReason(dto.getReason());
        form.setActive(true);
        // 注意：flowLogs 通常不從這裡塞入，因為會透過流程控制創建
        return form;
    }
	public LeaveForm toEntity(LeaveFormDto dto, Flow flow) {
        LeaveForm form = new LeaveForm();
        form.setCurrentFlow(flow);     
        form.setStartDate(dto.getStartDate());
        form.setEndDate(dto.getEndDate());
        form.setReason(dto.getReason());
        form.setActive(true);
        return form;
    }
}
