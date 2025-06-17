package com.jc.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
@Entity
public class MasterAddress {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer id;
	@ManyToOne
	private User user;

   @Lob
   @Column(length = 1000)
   private String address;
   

public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}  
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}

public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
}
