package com.jc.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.dto.UserDto;
import com.jc.demo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String email);
//	Optional findById(Integer id);

	List<User> findByStateIgnoreCase(String state);
    
	User findByResetToken(String token);
}
