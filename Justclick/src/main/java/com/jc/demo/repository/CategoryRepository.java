package com.jc.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.dto.CategoryDto;
import com.jc.demo.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	boolean existsByName(String name);
	
	List<Category> findByIsActiveTrue();

}
