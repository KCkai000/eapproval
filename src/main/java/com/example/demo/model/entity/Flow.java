package com.example.demo.model.entity;


import java.util.List;

import com.example.demo.enums.Action;
import com.example.demo.enums.Goto;
import com.example.demo.enums.State;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "flow")
public class Flow {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; // 流程ID
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state", length = 20)
	private State state;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "goto")
	private Goto goTo;  //
	
	@Enumerated(EnumType.STRING)
	@Column(name = "action")
	private Action action; // 動作
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@OneToMany(mappedBy = "flow")
	private List<FlowLog> flowLogs; // 一個流程可以有多個流程紀錄
	
	public Flow() {
		this.action = Action.START; // 預設動作為開始
		this.goTo = Goto.USER; // 預設流程開始於使用者提交表單
	}
}

