package com.jc.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.model.ProductOrder;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Integer> {
	

	ProductOrder findByOrderId(Integer id);
	
	List<ProductOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);


}
