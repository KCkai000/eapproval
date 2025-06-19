package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.service.RoleService;

@SpringBootTest
public class RoleAddTest {
	
	@Autowired
	private RoleService	roleService;
	
	@Test
	public void testRoleAdd() {
		roleService.addRole("Staff");  //一般員工
		System.out.println("Role add ok!");
		roleService.addRole("Manager"); //經理(主管)
		System.out.println("Role add ok!");
		roleService.addRole("Human Resources"); //人資
		System.out.println("Role add ok!");
		roleService.addRole("Admin");  // 系統管理員
		System.out.println("Role add ok!");
	}
}
