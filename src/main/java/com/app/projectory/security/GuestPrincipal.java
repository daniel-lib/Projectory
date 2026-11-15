package com.app.projectory.security;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class GuestPrincipal implements UserDetails {
  private static final long serialVersionUID = 1L;
  private final String participantId;
  private final String workspaceId;
  private final String displayName;

  public GuestPrincipal(String participantId, String workspaceId, String displayName) {
    this.participantId = participantId;
    this.workspaceId = workspaceId;
    this.displayName = displayName;
  }

  public String getParticipantId() {
    return participantId;
  }

  public String getWorkspaceId() {
    return workspaceId;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_GUEST"));
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return participantId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
