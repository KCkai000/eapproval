package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowDto {
	
	private Integer id;
	private String state; // 流程狀態
	private String goTo; // 流程轉向
	private String action; // 動作
	private Integer roleId; // 角色ID
	
}
