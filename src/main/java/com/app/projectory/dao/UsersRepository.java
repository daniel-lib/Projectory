package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.entity.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
	
	String findByUsername(String username);
	
	 List<Users> findAll();
	 
	 @Query(value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
		Users findUserByUsername(String username); 
}
