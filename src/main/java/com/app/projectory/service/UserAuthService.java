package com.app.projectory.service;

import com.app.projectory.entity.Users;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService implements UserLoginServiceInterface {

  public UserAuthService() {}

  @Override
  public Users authenticateUser(String username, String password, List<Users> allUsers) {
    // TODO Auto-generated method stub
    return null;
  }
}
