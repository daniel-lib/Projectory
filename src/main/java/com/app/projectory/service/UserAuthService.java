package com.app.projectory.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.projectory.entity.Users;

@Service
public class UserAuthService implements UserLoginServiceInterface{
	
	
	public UserAuthService() {
	}
	public List<Users> populateUserTest() {
		List<Users> listOfUsers = new ArrayList();
//		listOfUsers.add(new Users("admin", "admin123", "aba", "abu", "admin@gmail.com"));
//		listOfUsers.add(new Users("user1", "user123", "userela", "uno", "user1@gmail.com"));
//		listOfUsers.add(new Users("user2", "user123", "duo ", "userella","user2@yahoo.com"));

//		userDao.save(u);
		return listOfUsers;
	}
	
	public Users authenticateUser(String username, String password, List<Users> allUsers) {
		
		for(Users user : allUsers) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
//				return userAccount.get(username);
				return user;
			}
		}
		return null;
	}
	
	public int doesUserExist(String username, List<Users> allUsers) {
		for(Users user : allUsers) {
			if(user.getUsername().equals(username)) {
				return 1;
			}
		}
		
		return -1;
	}
	
	
	
	/*
	 * public Users getByUsername(String username, List<Users> allUsers) { for(Users
	 * user : allUsers) { if(user.getUsername().equals(username)) { return
	 * userAccount.get(username); } }
	 * 
	 * return null; }
	 */
}
