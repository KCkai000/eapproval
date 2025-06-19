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
public class FlowDto {
	
	private Integer id;
	private String state; // 流程狀態
	private String currentgoTo; //目前流程
	private String goTo; // 流程轉向
	private String action; // 動作
	private Integer roleId; // 角色ID
	private String stateLabel;//中文狀態描述(for 前端)

	

}
