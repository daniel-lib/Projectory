package com.app.projectory.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.projectory.entity.Users;

public class UserLoginService implements UserLoginServiceInterface{
	
	Map<String, Users> userAccount = new HashMap<>();
	
	public UserLoginService() {
		userAccount.put("admin", new Users("admin", "admin123", "aba", "abu"));
		userAccount.put("user1", new Users("user1", "user123", "userela", "uno"));
		userAccount.put("user2", new Users("user2", "user123", "duo ", "userella"));
	}
	
	public Users authenticateUser(String username, String password) {
		if(userAccount.containsKey(username) && userAccount.get(username).getPassword().equals(password)) {
			return userAccount.get(username);
		}
		return null;
	}
	
	public Users getByUsername(String username) {
		if(userAccount.containsKey(username)) {
			return userAccount.get(username);
		}
		return null;
	}
}
