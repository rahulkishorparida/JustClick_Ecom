package com.jc.demo.service.impl;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jc.demo.model.OtpToken;
import com.jc.demo.repository.OtpServiceRepository;
import com.jc.demo.repository.ProductOrderRepository;
import com.jc.demo.service.OtpService;
@Service
public class OtpServiceImpl implements OtpService {
	@Autowired
	private OtpServiceRepository  otpServiceRepository; 
    @Autowired
    private JavaMailSender mailSender;

	@Override
	public void generateAndSendOtp(String email) {
		int otp = ThreadLocalRandom.current().nextInt(100000, 999999); // 6-digit
		String valueOf = String.valueOf(otp);
		LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
		
		OtpToken  token = new OtpToken();
		token.setEmail(email);
		token.setExpiryTime(expiry);
		token.setOtp(valueOf);
		otpServiceRepository.save(token);

		sendOtpEmail(email, valueOf);

		 
	}

	@Override
	public void sendOtpEmail(String to, String otp) {
		  SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject("Verify Your Email - OTP");
	        message.setText("Your OTP is: " + otp + "\nIt is valid for 10 minutes.");
	        mailSender.send(message);
	}

	@Override
	public boolean verifyotp(String email, String otp) {
	 OtpToken token = otpServiceRepository.findByEmail(email);
	        if (token != null && token.getOtp().equals(otp) && token.getExpiryTime().isAfter(LocalDateTime.now())) {
	            otpServiceRepository.delete(token); // delete used token
	            return true;
	        }
	        return false;
	    }
	

}
