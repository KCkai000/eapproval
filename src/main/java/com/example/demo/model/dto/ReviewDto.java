package com.example.demo.model.dto;

import com.example.demo.enums.Action;

import lombok.Data;

@Data
public class ReviewDto {
	
	private Integer formId;
	private Action action;
}
