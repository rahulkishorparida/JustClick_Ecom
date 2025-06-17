package com.jc.demo.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private  long phone;
	private String email;
	private String city;
	private String address;
	private String state;
	private int pin;
	private String password;
	private String photo;
	
	private String role;
	@Column(nullable = false)
	private Boolean isEnable = true;
	@Column(nullable = false)
	private Boolean acccountNonLock = true ;

	@Column(nullable = false)
	private Integer failAttempt = 0;
	private Date lockTime;
	
	private String resetToken;
	
//	private boolean verified;
	
	
//	public boolean isVerified() {
//		return verified;
//	}
//	public void setVerified(boolean verified) {
//		this.verified = verified;
//	}
	public Boolean getAcccountNonLock() {
		return acccountNonLock;
	}
	public void setAcccountNonLock(Boolean acccountNonLock) {
		this.acccountNonLock = acccountNonLock;
	}
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

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
