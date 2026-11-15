package com.app.projectory.security;

import com.app.projectory.service.GuestSessionService;
import com.app.projectory.service.SystemAccessTrackerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain secFilterChain(
      HttpSecurity http,
      GuestSessionService guestSessionService,
      SystemAccessTrackerService accessTracker)
      throws Exception {
    http.addFilterBefore(
        new GuestAuthenticationFilter(guestSessionService),
        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    http.authorizeHttpRequests(authorizeHttpRequests ->
            authorizeHttpRequests
                .antMatchers("/guest/start", "/guest/join")
                .permitAll()
                .antMatchers("/guest/**")
                .hasRole("GUEST")
                .antMatchers(
                    "/user", "/user/**", "/project/**", "/todo/**", "/todo-list-collection/**")
                .hasRole("USER")
                .antMatchers("/", "/**")
                .permitAll())
        .formLogin()
        .loginPage("/?auth_required")
        .loginProcessingUrl("/login")
        .successHandler(new LoginSuccessHandler(accessTracker, guestSessionService));
    return http.build();
  }
}
