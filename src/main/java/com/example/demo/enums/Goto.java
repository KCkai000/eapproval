package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Goto {
	USER("使用者提送表單"),
	REVIEW_MANAGER("主管審核"),
	REVIEW_HR("人資最終確認");
	
	
	
	private final String displayName; // 描述	
	
	 Goto(String description) {
		this.displayName = description;		
	}
	public static Goto fromDisplayName(String displayName) {
		for (Goto type : Goto.values()) {
			if (type.getDisplayName().equals(displayName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid display name: " + displayName);
	}
}
	
	
