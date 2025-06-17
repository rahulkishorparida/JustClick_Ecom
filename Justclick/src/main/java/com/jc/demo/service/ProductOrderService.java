package com.jc.demo.service;

import java.time.LocalDate;
import java.util.List;

import com.jc.demo.dto.OrderRequestDto;
import com.jc.demo.model.ProductOrder;

public interface ProductOrderService {

	public ProductOrder  saveOrder(Integer userid ,OrderRequestDto orderRequestDto);
	
	public List<ProductOrder> getAllProductOrders();
	
	public Boolean updateOrderStatus(Integer id,String status);

	public ProductOrder getOrderById(Integer id);
	
	public ProductOrder getOrderByOrderId(Integer id);
	
	List<ProductOrder> getOrdersBetweenDates(LocalDate start, LocalDate end);

}
