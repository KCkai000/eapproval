package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlowLogDto {
	
	private Integer id; // 流程紀錄ID
	private String leaveForm; // 請假單資訊
	private String flow; // 流程資訊
	private String user; // 使用者資訊
	private String createTime; // 創建時間，格式化為字串以便於前端顯示

	// 可以根據需要添加其他屬性或方法
	
}
