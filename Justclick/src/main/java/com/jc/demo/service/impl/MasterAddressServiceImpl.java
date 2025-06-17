package com.jc.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.demo.model.MasterAddress;
import com.jc.demo.model.User;
import com.jc.demo.repository.MasterAddressRepository;
import com.jc.demo.repository.UserRepository;
import com.jc.demo.service.MasterAddressService;

@Service
public class MasterAddressServiceImpl implements MasterAddressService {

    @Autowired
    private MasterAddressRepository masterAddressRepository;
    @Autowired
    public UserRepository userRepository;
    
//    @Override
//    public List<MasterAddress> getAllAddress() {
//        return masterAddressRepository.findAll();
//    }

    @Override
    public boolean deleteAddress(Integer id) {
        if (masterAddressRepository.existsById(id)) {
            masterAddressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public MasterAddress findAddress(Integer id) {
        Optional<MasterAddress> optional = masterAddressRepository.findById(id);
        return optional.orElse(null);
    }
    
    @Override
    public MasterAddress saveAddress(MasterAddress masterAddress, Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
   
        if (userOptional.isPresent()) {
            masterAddress.setUser(userOptional.get());
            return masterAddressRepository.save(masterAddress);
            
//            User user = userOptional.get();
//            masterAddress.setUser(user);
//            return masterAddressRepository.save(masterAddress);
        } else { 
            throw new RuntimeException("User not found with id: " + userId);
            //   return null; // Or throw an exception if user not found
        }
    }

	@Override
	public List<MasterAddress> getAllMasterAddresses() {
		List<MasterAddress> all = masterAddressRepository.findAll();
		return all;
	}

	@Override
	public List<MasterAddress> getAddressesByUserId(Integer userId) {
		List<MasterAddress> byUserId = masterAddressRepository.findByUserId(userId);
		return byUserId;
	}

	@Override
	public boolean deleteAddressByUserId(Integer id) {
		masterAddressRepository.deleteById(id);
		return true;
	
	}

}
