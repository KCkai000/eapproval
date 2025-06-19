package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.Action;
import com.example.demo.enums.State;
import com.example.demo.model.entity.LeaveForm;

@Repository
public interface LeaveFormRepository extends JpaRepository<LeaveForm, Integer>{
	List<LeaveForm> findAll(); // 搜尋所有假單
	Optional<LeaveForm> findById(Integer id); // 根據假單ID搜尋假單
	@Query("SELECT l FROM LeaveForm l JOIN FETCH l.flowLogs WHERE l.id = :formId")
	Optional<LeaveForm> findWithFlowLogsById(@Param("id") Integer id); // 根據假單ID搜尋假單並包含流程紀錄
	@Query("SELECT l FROM LeaveForm l WHERE l.state.state = :state")
	List<LeaveForm> findByState(@Param("state") State state);
//	@Query("SELECT l FROM LeaveForm l WHERE l.action.action = :action AND l.action.name = :roleName") // 根據動作和角色搜尋假單
//	List<LeaveForm> findPendingFormsByActionAndRole(@Param("action") Action action, @Param("roleName") String roleName);
	@Query("SELECT l FROM LeaveForm l WHERE l.active = true")
	List<LeaveForm> findAllActive();
	List<LeaveForm> findByActionActionAndActionRoleName(Action action, String roleName);
	
	List<LeaveForm> findLeaveFormsBUserId();
}
