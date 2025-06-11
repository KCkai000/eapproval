package com.example.demo.model.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "leave_form")
public class LeaveForm {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; // 請假單ID
	@OneToMany
	@Column(name = "flow_state")
	private Flow flowState; // 假別
	@ManyToOne
	@JoinColumn(name = "flow_action")
	private Flow action; // 審核狀態
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate; // 結束日期
	@Column(name = "reason", length= 200)
	private String reason; // 請假原因
	@OneToMany(mappedBy = "leaveForm")
	private List<FlowLog> flowLogs; // 一個請假單可以有多個流程紀錄
}
