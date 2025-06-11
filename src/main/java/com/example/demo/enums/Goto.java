package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Goto {
	USER("使用者提送表單", "null", "REVIEW1"),
	REVIEW1("主管審核", "USER","REVIEW2"),
	REVIEW2("人資最終確認", "REVIEW2", "null");
	
	
	private final String description; // 描述
	private final String previousStep; // 從哪個節點來
	private final String nextStep; // 到哪個節點去
	
	 Goto(String description, String previousStep, String nextStep) {
		this.description = description;
		this.previousStep = previousStep;
		this.nextStep = nextStep;
	}
	
	public Goto getPreviousStep() throws IllegalArgumentException {
		return previousStep == null ? null : Goto.valueOf(previousStep);
	}
	
	public Goto getNextStep() throws IllegalArgumentException {
		return nextStep == null ? null : Goto.valueOf(nextStep);
	}
}
