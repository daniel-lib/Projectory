package com.app.projectory.service;

import com.app.projectory.entity.Users;
import java.util.List;

public interface UserLoginServiceInterface {
  public Users authenticateUser(String username, String password, List<Users> allUsers);
}
