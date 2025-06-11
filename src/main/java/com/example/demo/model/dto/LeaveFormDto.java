package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveFormDto {
	private Integer id; // 請假單ID
	private String flowState; // 假別
	private String action; // 審核狀態
	private String startDate; // 開始日期
	private String endDate; // 結束日期
	private String reason; // 請假原因
	private String flowLogs; // 流程紀錄，這裡可以根據實際需要調整類型或結構
}
