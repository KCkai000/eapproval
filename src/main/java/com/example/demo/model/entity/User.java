package com.example.demo.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;  //使用者id
	@Column(name="name", nullable = false, length =60)
	private String username; //使用者姓名
	@Column(name = "mail", length = 100, nullable = false)
	private String email; // 使用者信箱
	@Column(name="account", unique = true, nullable = false, length =60)
	private String account; //使用者帳號	
	@Column(name= "password", nullable = false)
	private String password; //使用者密碼
//	@Column(name = "salt", nullable = false)
//	private String salt;  //鹽
	@Column(name = "active")  
	private Boolean active = true;   //預設為建立即啟用
	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private Role role;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<FlowLog> flowLogs; // 一個使用者可以有多個流程紀錄
	
	public User(String username, String email, String account, String password, Boolean active, Role role) {
		
		this.username = username;
		this.email = email;
		this.account = account;
		this.password = password;
		this.active = active;
		this.role = role;
	}
}

