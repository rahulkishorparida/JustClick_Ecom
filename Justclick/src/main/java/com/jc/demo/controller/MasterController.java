package com.jc.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jc.demo.service.MasterAddressService;
import com.jc.demo.service.UserService;

public class MasterController {
	
    @Autowired
    private MasterAddressService masterAddressService;
    @Autowired
    private UserService userService;

    @GetMapping("/select-address")
    public String showAddressDropdown( Model model) {
    	
    	
    	
		return null;
    	
    	
      
    }

}
