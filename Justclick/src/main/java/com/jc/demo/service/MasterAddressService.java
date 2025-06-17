package com.jc.demo.service;

import java.util.List;

import com.jc.demo.dto.CategoryDto;
import com.jc.demo.model.MasterAddress;

public interface MasterAddressService {
	
	public MasterAddress saveAddress(MasterAddress masterAddress, Integer userId);
	
//	public List<MasterAddress> getAllAddress();
	
	public boolean deleteAddress(Integer id);
	
	public MasterAddress findAddress(Integer id);
	
	public List<MasterAddress> getAllMasterAddresses();
	
	public List<MasterAddress> getAddressesByUserId(Integer userId);
	
	public boolean deleteAddressByUserId(Integer id);
	
}

