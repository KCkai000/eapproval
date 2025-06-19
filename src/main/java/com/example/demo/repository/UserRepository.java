package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{
	
	@Query("SELECT u FROM User u JOIN FETCH u.role r WHERE r.id = :roleId")
	List<User> findAllByRole(@Param("roleId") Integer roleId);	
	List<User> findByActive(Boolean active);
	List<User> findAll();
	@Query("SELECT u From User u WHERE u.username LIKE %:keyword% OR u.account LIKE %:keyword% OR u.email LIKE %:keyword%")
	List<User> searchByUsernameOrAccountOrEmail(@Param("keyword") String keyword);
	Optional<User> findById(Integer id);
	List<User> findByUsername(String username);
	@Query("SELECT u FROM User u Join FETCH u.role WHERE u.account = :account")
	Optional<User> findByAccount(String account);
	Optional<User> findByEmail(String email);
	
}
