package com.app.projectory.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.app.projectory.dao.UsersRepository;
import com.app.projectory.entity.Users;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailsServTest {

  @Mock private UsersRepository userRepo;

  @InjectMocks private UserDetailsServ userDetailsServ;

  @Test
  void acceptsRoleWithSpringSecurityPrefix() {
    assertRoleMapsToRoleUser("ROLE_USER");
  }

  @Test
  void acceptsRoleWithoutSpringSecurityPrefix() {
    assertRoleMapsToRoleUser("USER");
  }

  private void assertRoleMapsToRoleUser(String role) {
    Users user = new Users();
    user.setUsername("testuser");
    user.setPassword("encoded-password");
    user.setRole(role);
    when(userRepo.findUserByUsername("testuser")).thenReturn(user);

    UserDetails userDetails = userDetailsServ.loadUserByUsername("testuser");

    assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
  }
}
