package com.jc.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.model.OtpToken;

public interface OtpServiceRepository extends JpaRepository<OtpToken, Long> {
	
	 OtpToken findByEmail(String email);

}
