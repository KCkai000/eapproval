package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;
import com.example.demo.model.entity.LeaveForm;

@Repository
public interface LeaveFormRepository extends JpaRepository<LeaveForm, Integer>{
	List<LeaveForm> findAll(); // 搜尋所有假單
	Optional<LeaveForm> findById(Integer id); // 根據假單ID搜尋假單
	@Query("SELECT l FROM LeaveForm l JOIN FETCH l.flowLogs WHERE l.id = :formId")
	Optional<LeaveForm> findWithFlowLogsById(@Param("formId") Integer id); // 根據假單ID搜尋假單並包含流程紀錄
	@Query("SELECT l FROM LeaveForm l WHERE l.currentFlow.state = :state")
	List<LeaveForm> findByState(@Param("state") State state);
//	@Query("SELECT l FROM LeaveForm l WHERE l.action.action = :action AND l.action.name = :roleName") // 根據動作和角色搜尋假單
//	List<LeaveForm> findPendingFormsByActionAndRole(@Param("action") Action action, @Param("roleName") String roleName);
	@Query("SELECT l FROM LeaveForm l WHERE l.active = true")
	List<LeaveForm> findAllActive();
//	@Query("SELECT l FROM LeaveForm l WHERE l.currentFlow.action = :action AND l.currentFlow.roleName = :roleName")
//	List<LeaveForm> findByActionAndRoleName(Action action, String roleName);
	@Query("SELECT DISTINCT f.leaveForm FROM FlowLog f WHERE f.user.id = :userId")
	List<LeaveForm> findAllByUserIdFromLogs(@Param("userId") Integer userId);
	@Query("SELECT f FROM LeaveForm f WHERE f.currentFlow.role.id =:roleId AND f.currentFlow.action IN :actions")
	List<LeaveForm> findPendingFormsByRoleId(@Param("roleId") Integer roleId, @Param("actions") List<Action> actions);
	@Query("SELECT f FROM LeaveForm f WHERE f.currentFlow.action IN :actions AND f.currentFlow.role.name = :roleName")
	List<LeaveForm> findFormsByActionsAndRoleName(@Param("actions") List<Action> actions, @Param("roleName") String roleName);
	@Query("SELECT f FROM LeaveForm f WHERE f.currentFlow.currentgoTo = :goTo AND f.currentFlow.action = :action AND f.active = true")
	List<LeaveForm >  findPendingByGoToAndAction(@Param("goTo") Goto goTo, @Param("action") Action action);
	// 用於審核流程中，搜尋節點用的
	@Query("SELECT f FROM LeaveForm f WHERE f.currentFlow.goTo = :goTo")
	List<LeaveForm> findByCurrentFlow_GoTo(@Param("goTo") Goto goTo);
}
