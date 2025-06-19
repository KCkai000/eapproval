package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum Action {
	START(0, "開始"),
	SUBMITTED(1, "已送出"),
	PENDING(2, "待審核"),
	APPROVED(3, "已批准"),
	REJECTED(4, "已拒絕"),
	CANCELLED(5, "已取消"),
	RETURNED(6, "已退回"),
	END(-1, "已完成");
	
	
	
	
	private final Integer code;
	private final String displayName;
	
	Action(Integer code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}
	
	public static Action fromCode(Integer code) {
		for (Action type : Action.values()) {
			if (type.getCode().equals(code)) {
				return type;
			}
		}
		return null;
	}
	
	public static Action fromDisplayName(String displayName) {
		for (Action type : Action.values()) {
			if (type.getDisplayName().equals(displayName)) {
				return type;
			}
		}
		return null;
	}
}
