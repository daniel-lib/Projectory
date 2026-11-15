package com.app.projectory.security;

import com.app.projectory.service.GuestSessionService;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class GuestAuthenticationFilter extends OncePerRequestFilter {
  private final GuestSessionService sessionService;

  public GuestAuthenticationFilter(GuestSessionService sessionService) {
    this.sessionService = sessionService;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    return !path.startsWith("/guest/") || "/guest/start".equals(path) || "/guest/join".equals(path);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        && !"anonymousUser"
            .equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = readCookie(request);
    Optional<GuestPrincipal> principal = sessionService.resolve(token);
    if (!principal.isPresent()) {
      sessionService.revoke(token);
      GuestCookie.clear(response, request.isSecure());
      response.sendRedirect(request.getContextPath() + "/?guest_expired");
      return;
    }

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            principal.get(), null, principal.get().getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    try {
      filterChain.doFilter(request, response);
    } finally {
      SecurityContextHolder.getContext().setAuthentication(null);
      SecurityContextHolder.clearContext();
    }
  }

  private String readCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (GuestCookie.NAME.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
