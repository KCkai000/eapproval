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
import com.example.demo.model.entity.Flow;

@Repository
public interface FlowRepository extends JpaRepository<Flow, Integer>{
	
	@Query("SELECT f FROM Flow f Where f.state = :state")
	List<Flow> findByState(@Param("state") State state);
	@Query("SELECT f FROM Flow f Where f.action = :action")
	List<Flow> findByAction(@Param("action") Action action);
	@Query("SELECT f FROM Flow f WHERE f.state = :state AND f.action = :action And f.role.id = :roleId")
	Optional<Flow> findByStateAndActionAndRoleId(@Param("state") State state, @Param("action") Action action, @Param("roleId") Integer roleId); // ← 用這個最保險
	@Query("SELECT f FROM Flow f WHERE f.goTo = :goTo AND f.action = :action")
	Optional<Flow> findByGotoAndAction(@Param("goTo") Goto goTo, @Param("action") Action action); 
	@Query("SELECT f FROM Flow f WHERE f.state = :state AND f.action = :action AND f.currentgoTo = :currentgoTo")
	Optional<Flow> findByStateAndActionAndCurrentgoTo(@Param("state") State state, @Param("action") Action action, @Param("currentgoTo") Goto currentgoTo);
	// 用來找下一個流程的
	@Query("SELECT f FROM Flow f WHERE f.currentgoTo = :currentGoTo And f.state = :state")
	List<Flow> findByCurrentGoToAndState(@Param("currentGoTo") Goto currentGoTo, @Param("state") State state);
	//@Query("SELECT f FROM Flow f WHERE f.current = :currentgoTo AND f.state = :state AND f.action = :action AND f.role.id = :roleId")
	Optional<Flow> findByCurrentgoToAndStateAndActionAndRole_Id(@Param("current") Goto currentgoTo, @Param("state") State state, @Param("action") Action action, @Param("roleId") Integer roleId);
	
	@Query("SELECT f FROM Flow f WHERE f.currentgoTo = :currentgoTo AND f.state = :state AND f.action = :action")
	List<Flow> findByCurrentAndStateAndAction(@Param("currentgoTo") Goto currentgoTo,
	                                          @Param("state") State state,
	                                          @Param("action") Action action);
	// 使用 當前節點、下個節點以及假別 篩選流程
	@Query("SELECT f FROM Flow f WHERE f.currentgoTo = :currentgoTo AND f.goTo = :goTo AND f.state = :state")
	Optional<Flow> findByTransition(@Param("currentgoTo") Goto currentgoTo, @Param("goTo") Goto goTo, @Param("state") State state);
	
	//用於審核流程中判斷是否為終點流程
	@Query("SELECT f FROM Flow f WHERE f.currentgoTo =:currentgoTo AND f.goTo IS NULL AND f.state = :state AND f.action = :action")
	Optional<Flow> findFinalStep(@Param("currentgoTo") Goto currentgoTo,  @Param("state") State state, @Param("action") Action action);
}
