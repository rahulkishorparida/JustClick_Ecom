package com.jc.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.model.MasterAddress;
import com.jc.demo.model.User;

public interface MasterAddressRepository  extends JpaRepository<MasterAddress, Integer>{
	
    Optional<MasterAddress> findByUserEmail(String email); 
 
    List<MasterAddress> findByUserId(Integer userId);


}
