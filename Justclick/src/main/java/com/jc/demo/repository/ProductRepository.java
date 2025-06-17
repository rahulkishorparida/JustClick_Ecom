package com.jc.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.dto.ProductDto;
import com.jc.demo.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
    boolean existsByName(String name);
    List<Product> findByIsActiveTrue();
    
    
    List<Product> findByIsActiveTrueAndCategoryIgnoreCase(String category);
    
    List<Product> findByIsActiveTrueAndBrandIgnoreCase(String brand);

    List<Product> findByphoto(String photo);
    
    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String ch1, String ch2);

}
