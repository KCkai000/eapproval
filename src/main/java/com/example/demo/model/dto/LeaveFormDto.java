package com.example.demo.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeaveFormDto {
	private Integer id; // 請假單ID
	private String flowState; // 假別
	private String action; // 審核狀態
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate; // 開始日期
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate; // 結束日期
	private String reason; // 請假原因
	private List<FlowLogDto> flowLogs; // 流程紀錄，這裡可以根據實際需要調整類型或結構
}
