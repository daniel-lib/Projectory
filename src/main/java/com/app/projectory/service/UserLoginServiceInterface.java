package com.app.projectory.service;

import com.app.projectory.entity.Users;

public interface UserLoginServiceInterface {
	public Users authenticateUser(String username, String password);
}
