package com.app.projectory.security;

import com.app.projectory.service.GuestSessionService;
import com.app.projectory.service.SystemAccessTrackerService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
  private final SystemAccessTrackerService accessTracker;
  private final GuestSessionService guestSessionService;

  public LoginSuccessHandler(
      SystemAccessTrackerService accessTracker, GuestSessionService guestSessionService) {
    this.accessTracker = accessTracker;
    this.guestSessionService = guestSessionService;
    setDefaultTargetUrl("/user/dashboard?auth_successful");
  }

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    accessTracker.record(request, false, authentication.getName());
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (GuestCookie.NAME.equals(cookie.getName())) {
          guestSessionService.revoke(cookie.getValue());
          break;
        }
      }
    }
    GuestCookie.clear(response, request.isSecure());
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
