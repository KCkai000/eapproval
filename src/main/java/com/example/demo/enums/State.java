package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum State {
	PERSONAL_LEAVE(1, "事假"),    
	SICK_LEAVE(2, "病假"),    //SICK_LEANE =2
	ANNUAL_LEAVE(3, "特休假"),  //ANNUAL_LEAVE = 3
	FUNERAL_LEAVE(4, "喪假"),   //FUNERAL_LEAVE = 4
	OFFICIAL_LEAVE(5, "公假"),  //OFFCIAL_LEAVE = 5
	OTHER_LEAVE(6, "其他假別");  //OTHER_LEAVE = 6
	
	
	private final Integer code;
	private final String displayName;
	
	State(Integer code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}
	
	// 根據輸入數字 返回對應的假別
	public static State fromCode(Integer code) {
		for(State type : State.values()) {
			if(type.code.equals(code)) {
				return type;
			}
		}
		return null;
	}
	
	public static State fromDisplayName(String displayName) {
		if(displayName == null || displayName.isBlank()) {
			throw new IllegalArgumentException("State displayName 不可為null及空白");
		}
		for(State state : State.values()) {
			if(state.getDisplayName().equals(displayName)) {
				return state;
			}
		}
		throw new IllegalArgumentException("找不到對應的 State displayName: " + displayName);
	}

}
