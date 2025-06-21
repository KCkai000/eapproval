package com.example.demo.model.entity;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.enums.Action;
import com.example.demo.enums.State;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "leave_form")
public class LeaveForm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; // 請假單ID	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private State state ; // 假別	
	@Column(name = "flow_action")
	@Enumerated(EnumType.STRING)
	private Action action; // 審核狀態
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate; // 結束日期
	@Column(name = "reason", length= 200)
	private String reason; // 請假原因
	@Column(name = "active", nullable = false) //作為表單是否存在在頁面 false =>軟刪除
	private Boolean active = true; // 是否啟用，預設為true
	@OneToMany(mappedBy = "leaveForm", fetch = FetchType.LAZY)	
	@JsonIgnore
	private List<FlowLog> flowLogs; // 一個請假單可以有多個流程紀錄
	@ManyToOne
	@JoinColumn(name = "current_flow_id")
	private Flow currentFlow; // 與flow表關聯，表示當前流程狀態
}
