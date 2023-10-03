package com.app.projectory.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	DataSource dataSource;
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)throws Exception{
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("SELECT username, password, enabled FROM users where username = ?")
			.authoritiesByUsernameQuery("SELECT username, role FROM users where username = ?")
			.passwordEncoder(encoder);	
	}
	
	
	
	//override a configure method to set the access rules
	@Override
	protected void configure(HttpSecurity http) throws Exception{
			http.authorizeRequests()
				//.antMatchers("/user","/user/**").authenticated()
				.antMatchers("/user").hasRole("USER")
				.antMatchers("/user/**").hasRole("USER")
				.antMatchers("/", "/**").permitAll()				
				.and().formLogin();				
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	}
	
	/*
	 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
	 * Exception { http .authorizeHttpRequests((authz) -> authz
	 * .anyRequest().authenticated() ) .httpBasic(withDefaults()); return
	 * http.build(); }
	 * z
	 * @Bean public WebSecurityCustomizer webSecurityCustomizer() { return (web) ->
	 * web.ignoring().antMatchers("/", "/ignore2"); }
	 */
    
    
}
