package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Type {
	START(1, "起始節點"),
	END(2, "結束節點");
	
	private final int code;
	private final String description;
	
	private Type(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static Type fromCode(int code) {
		for (Type type : Type.values()) {
			if (type.code == code) {
				return type;
			}
		}
		return null; // 或者拋出異常
	}
}
