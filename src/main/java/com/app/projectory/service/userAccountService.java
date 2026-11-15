package com.app.projectory.service;

import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class userAccountService {

  @Autowired UsersRepository userDao;

  public userAccountService() {}

  public long getUserId(Authentication auth) {
    // get user by username
    Users user = getCurrentUserDetail(auth);

    // get user id from the user object
    long userId = user.getUserId();

    return userId;
  }

  public String getCurrentUsername(Authentication auth) {
    return auth.getName();
  }

  public Users getCurrentUserDetail(Authentication auth) {
    return userDao.findUserByUsername(getCurrentUsername(auth));
  }

  public Users getUserDetailByUsername(String username) {
    return userDao.findUserByUsername(username);
  }
}
