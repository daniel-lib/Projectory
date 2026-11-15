package com.app.projectory.service;

import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServ implements UserDetailsService {
  @Autowired UsersRepository userRepo;

  @Override
  public UserDetails loadUserByUsername(String username) {
    Users user = userRepo.findUserByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException(username);
    }

    String role = user.getRole();
    if (role.startsWith("ROLE_")) {
      role = role.substring("ROLE_".length());
    }

    return User.withUsername(user.getUsername())
        .password(user.getPassword())
        .roles(role)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();

    //		return org.springframework.security.core.userdetails.User
    //	            .withUsername(user.getUsername())
    //	            .password(user.getPassword())
    //	            .roles(user.getRole())
    //	            .accountExpired(false)
    //	            .accountLocked(false)
    //	            .credentialsExpired(false)
    //	            .disabled(false)
    //	            .build();
  }
}
