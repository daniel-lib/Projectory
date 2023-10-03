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

	@Override
	public Users authenticateUser(String username, String password, List<Users> allUsers) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
