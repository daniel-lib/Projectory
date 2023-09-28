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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


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
	
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
			http.authorizeRequests()
			//.antMatchers("/register-user").permitAll()
				.antMatchers("/user","/user/**").authenticated()
				.antMatchers("/user").hasRole("USER")
				//.antMatchers("/user/**").authenticated()
				.antMatchers("/user/**").hasRole("USER")
//				.antMatchers("/db/**").permitAll()
				.antMatchers("/", "/**").permitAll()
//				.antMatchers("/").authenticated()
				.and().formLogin();
			
			//to get the h2 console working
			http.csrf().disable();
//			http.headers().frameOptions().disable();
				
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
