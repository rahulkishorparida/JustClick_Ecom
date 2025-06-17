package com.jc.demo.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class AuthSuccessHandlerImpl implements AuthenticationSuccessHandler {
	                                  //which allows custom post-login behavior

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	
//That’s a collection of GrantedAuthority objects, not strings.
	
	   Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
	   
//	Set<String> authorityListToSet = AuthorityUtils.authorityListToSet(authorities);
//	   Set<String> authorityListToSet = AuthorityUtils.authorityListToSet(authorities);
	   
 //Without AuthorityUtils, you'd need to manually loop over authorities and extract role names.
	   
	   if(roles.contains("ROLE_ADMIN")) {
		   response.sendRedirect("/admin/");
	   }else {
		response.sendRedirect("/");
	}
	}


}
