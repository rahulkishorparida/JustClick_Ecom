package com.jc.demo.dto;

import java.util.Date;

import jakarta.persistence.Column;

public class UserDto {
	
	private int id;
	private String name;
	private  long phone;

	private String email;
	private String city;
	private String address;
	private String state;
	private int pin;
	private String password;
	private String role;
	private Boolean isEnable;
	
	private Boolean acccountNonLock = true;


	public Boolean getAcccountNonLock() {
		return acccountNonLock;
	}
	public void setAcccountNonLock(Boolean acccountNonLock) {
		this.acccountNonLock = acccountNonLock;
	}

	private Integer failAttempt;
	private Date lockTime;
	
	private String resetToken;
	
		


	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public Integer getFailAttempt() {
		return failAttempt;
	}
	public void setFailAttempt(Integer failAttempt) {
		this.failAttempt = failAttempt;
	}
	public Date getLockTime() {
		return lockTime;
	}
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	private String photo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getPin() {
		return pin;
	}
	public void setPin(int pin) {
		this.pin = pin;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}


}
