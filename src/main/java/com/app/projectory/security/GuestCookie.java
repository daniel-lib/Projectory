package com.app.projectory.security;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

public final class GuestCookie {
  public static final String NAME = "projectory_guest";

  private GuestCookie() {}

  public static void set(
      HttpServletResponse response, String token, boolean secure, long maxAgeSeconds) {
    ResponseCookie cookie =
        ResponseCookie.from(NAME, token)
            .httpOnly(true)
            .secure(secure)
            .path("/")
            .maxAge(Math.max(0, maxAgeSeconds))
            .sameSite("Lax")
            .build();
    response.addHeader("Set-Cookie", cookie.toString());
  }

  public static void clear(HttpServletResponse response, boolean secure) {
    ResponseCookie cookie =
        ResponseCookie.from(NAME, "")
            .httpOnly(true)
            .secure(secure)
            .path("/")
            .maxAge(0)
            .sameSite("Lax")
            .build();
    response.addHeader("Set-Cookie", cookie.toString());
  }
}
