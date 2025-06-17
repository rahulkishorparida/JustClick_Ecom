package com.jc.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.jc.demo.model.User;
import com.jc.demo.repository.UserRepository;
import com.jc.demo.service.UserService;
import com.jc.demo.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String email = request.getParameter("username");
		
		//This extracts the value of the "username" field from the login form submitted by the user.
		User user = userRepository.findByEmail(email);
		
		if(user.getIsEnable()) {
			
			if(user.getAcccountNonLock()) {
				
				if(user.getFailAttempt()<AppConstant.ATTEMPT_TIME) {
					userService.increasefailedAttempt(user);
					
				}else {
					userService.userAcountLock(user);
					exception = new LockedException("your account is locked after 3 attempt.");	
				}
			}
			else {
				if(userService.unlockAcountTimeExpired(user)) {
					
					exception = new LockedException("your acount is uncloked!! pls try to log");
				}else {
					exception = new LockedException("your acount is locked , pls try agter some times");
				}
				
			}
		}else {
			exception = new LockedException("User acount is inactive");
		}
		super.setDefaultFailureUrl("/signin?error");
//SPRING_SECURITY_LAST_EXCEPTION is a Spring-managed session attribute holding the reason for failure.
// [[${session.SPRING_SECURITY_LAST_EXCEPTION.message}]]
		super.onAuthenticationFailure(request, response, exception);
	}
	
	
	
	
}
