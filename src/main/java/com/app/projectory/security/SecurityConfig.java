package com.app.projectory.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
//	@Autowired
//	DataSource dataSource;
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Bean
	//create security chain to set the access rules	
	public SecurityFilterChain secFilterChain(HttpSecurity http) throws Exception{
			http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
					.antMatchers("/user").hasRole("USER")
					.antMatchers("/user/**").hasRole("USER")
					.antMatchers("/project/**").hasRole("USER")
					.antMatchers("/", "/**").permitAll()
					)		
				.formLogin()
					.loginPage("/?auth_required")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/user/dashboard?auth_successful");
				//.and().formLogin().loginPage("/?auth=required").successForwardUrl("/user/dashboard");
			return http.build();							
			    
	}
	

    
}
