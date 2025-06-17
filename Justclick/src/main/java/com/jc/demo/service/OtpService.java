package com.jc.demo.service;

public interface OtpService {
	
    public void generateAndSendOtp(String email);
    
    public void sendOtpEmail(String to, String otp);
    
    public boolean verifyotp(String email, String otp);
    
    

}
