package com.app.projectory.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.projectory.dto.CurrentUserDetailDto;
import com.app.projectory.dto.PublicUserPersonalDetailDto;
import com.app.projectory.entity.Users;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {
	
	//String findByUsername(String username);
	Users findByUsername(String username);
	
	 List<Users> findAll();
	 
	 @Query(value = "SELECT * FROM users u WHERE u.username = ?1", nativeQuery = true)
		Users findUserByUsername(String username); 
	 
	 @Query(value="SELECT first_name firstName, last_name lastName, user_id userId, online_status onlineStatus, \n"
	 		+ "username, profile_picture profilePicture\n"
	 		+ "FROM users WHERE username = ?1", nativeQuery=true)
	 PublicUserPersonalDetailDto getPublicUserDetail(String username);
	 
	 @Query(value = "SELECT user_id userId, email_address emailAddress, enabled, \n"
	 		+ "first_name firstName, last_name lastName, online_status onlineStatus, \n"
	 		+ "role, username, profile_picture profilePicture\n"
	 		+ "FROM users\n"
	 		+ "WHERE user_id = ?1", nativeQuery=true)
	 CurrentUserDetailDto getCurrentUserDetail(long userId);	
}
