package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.LeaveFormDto;
import com.example.demo.model.entity.LeaveForm;
import com.example.demo.model.entity.User;

@Service
public interface LeaveFormService {
	//新增假單
	LeaveForm createLeaveForm(LeaveFormDto dto);
	//查詢假單	(全假單給管理者)
	List<LeaveForm> getAllLeaveForms();
	//查詢個人假單(顯示於個人頁面)
	List<LeaveFormDto> findLeaveFormDtos(Integer userId);
//	List<LeaveFormDto> findLeaveFormByUserId(@Param("userId") Integer userId);
	//更新假單
	LeaveForm updateLeaveForm(LeaveFormDto leaveForm);
	//刪除假單(只有在頁面上刪除 不動資料庫)
	void deactivateLeaveForm(Integer id);
	// 處理假單 (審核流程)
	void processLeaveForm()
	
}
