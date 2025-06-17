package com.jc.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.jc.demo.service.UserService;
import com.jc.demo.service.impl.UserServiceImpl;

@Configuration
public class SecurityConfig {
	
//	@Autowired
//	private  AuthSuccessHandlerImpl authSuccessHandlerImpl;
	
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	@Lazy
	private AuthFailureHandlerImpl authenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() { //  interface used to hash (encrypt) and verify passwords.
		return new  BCryptPasswordEncoder();	//secure hashing algorithm for passwords	
	}
	//Spring will create a singleton instance of PasswordEncoder and make it available 
	//wherever needed (e.g., for login, registration).
	
	@Bean
	public UserDetailsService userDetailsService() {
	    return new com.jc.demo.config.UserServiceImpl();  
	}
	// create and manage this object (UserDetailsService) as a Spring Bean
	//load user-specific data during authentication
	// custom implementation, UserServiceImpl, which fetches user data
	
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider authenticationProvider= new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	// built-in class to handle authentication using your custom UserDetailsService and password encoding.
		
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.csrf(csrf->csrf.disable()).cors(cors -> cors.disable())
		.authorizeHttpRequests(req->req.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/**").permitAll())
		
		.formLogin(form-> form.loginPage("/signin")
				.loginProcessingUrl("/login")
//				.defaultSuccessUrl("/"))
				.failureHandler(authenticationFailureHandler)
				.successHandler(authenticationSuccessHandler))
		.logout(logout-> logout.permitAll());
		return http.build();
		
		//security filter chain.
		//Only users with role USER can access /user/** endpoints.
	}


}
