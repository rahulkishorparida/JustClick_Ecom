package com.jc.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
    private String photo;
    private Boolean isActive;


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public Boolean getIsActive() {
	    return isActive;
	}
	public void setIsActive(Boolean isActive) {
	    this.isActive = isActive;
	}

}

	


//@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//private List<SubCategory> subCategories = new ArrayList();