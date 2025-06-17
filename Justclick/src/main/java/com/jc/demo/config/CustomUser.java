package com.jc.demo.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jc.demo.model.User;

public class CustomUser implements  UserDetails { 
	
	//adapts user object to  Spring Security's interface.

	private User user;
	
	public CustomUser(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	 SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(authority);
	}
	// It tells the framework what roles or permissions the current user has.
	
	 //Creating a Role-Based Authority
	 //retrive role from object

	 //user might have more than one role, and the getAuthorities() method is designed to return multiple authorities.

	@Override
	public String getPassword() {
	return	user.getPassword();
		
	}

	@Override
	public String getUsername() {
	return	user.getEmail();
		
	}
//	----

	@Override
	public boolean isAccountNonLocked() {
	    return user.getAcccountNonLock();
	}
	
	@Override
	public boolean isEnabled() {
	 return user.getIsEnable();
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true; 
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true; 
	}



	

}
