package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.FlowLog;
import com.example.demo.model.entity.LeaveForm;

@Repository
public interface FlowLogRepository extends JpaRepository<FlowLog, Integer>{
	
//	@EntityGraph(attributePaths = "leaveForm.flowLogs")
	@Query("SELECT DISTINCT f.leaveForm FROM FlowLog f JOIN FETCH f.leaveForm.flowLogs WHERE f.user.id = :userId")
	List<LeaveForm> findLeaveFormByUserId(@Param("userId") Integer userId); // 根據使用者ID搜尋假單
	// 用來儲存流程紀錄的
	@EntityGraph(attributePaths = "leaveForm.flowLogs")
	@Query("SELECT f From FlowLog f WHERE f.user.id = :userId")
	List<FlowLog> findByUserId(@Param("userId") Integer userId); // 根據使用者ID搜尋流程紀錄
}
