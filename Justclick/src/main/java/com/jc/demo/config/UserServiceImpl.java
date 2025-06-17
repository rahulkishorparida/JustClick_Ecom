package com.jc.demo.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jc.demo.model.User;
import com.jc.demo.repository.UserRepository;

public class UserServiceImpl implements UserDetailsService {
	// class tells Spring Security how to fetch user data (database) for login processing.
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User byEmail = userRepository.findByEmail(username);
    
    if(byEmail == null) {
    	throw new UsernameNotFoundException("User not found");
    }
    return new CustomUser(byEmail);
    //Spring Security expects a UserDetails object during login, not just any object like your User entity.
   // So you have to convert your User entity to a UserDetails object — that’s what CustomUser is for.

}
}
