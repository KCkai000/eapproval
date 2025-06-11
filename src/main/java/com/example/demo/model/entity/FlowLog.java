package com.example.demo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "flow_log")
public class FlowLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; // 流程紀錄ID
	
	@ManyToOne
	@JoinColumn(name = "leave_form_id")
	private LeaveForm leaveForm;
	
	@ManyToOne
	@JoinColumn(name = "flow_id")
	private Flow flow;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	@CreationTimestamp
	private LocalDateTime createTime;
}
