package com.jc.demo.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.demo.dto.OrderRequestDto;
import com.jc.demo.model.Cart;
import com.jc.demo.model.MasterAddress;
import com.jc.demo.model.OrderAddress;
import com.jc.demo.model.ProductOrder;
import com.jc.demo.repository.CartRepository;
import com.jc.demo.repository.MasterAddressRepository;
import com.jc.demo.repository.ProductOrderRepository;
import com.jc.demo.service.CategoryService;
import com.jc.demo.service.ProductOrderService;
import com.jc.demo.util.OrderStatus;
@Service
public class ProductOrderServiceImpl implements ProductOrderService {
     @Autowired
	private ProductOrderRepository productOrderRepository;
	@Autowired
     private CartRepository cartRepository;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private MasterAddressRepository masterAddressRepository;


	@Override
	public ProductOrder saveOrder(Integer userId, OrderRequestDto orderRequestDto) {
	    List<Cart> carts = cartRepository.findByUserId(userId);
	    
	    MasterAddress address = masterAddressRepository.findById(orderRequestDto.getSelectedAddress())
	        .orElseThrow(() -> new RuntimeException("Address not found"));
	    ProductOrder  lastorder= null;
	    for (Cart cart : carts) {
	        ProductOrder order = new ProductOrder();
//	        order.setOrderId(UUID.randomUUID().toString());
	        
	        Random random = new Random();
	        int orderId = 100000 + random.nextInt(900000); // 6-digit random ID
	        order.setOrderId(orderId);
	        
	        order.setOrderDate(LocalDate.now());
	        order.setProduct(cart.getProduct());
	        order.setFinalPrice(cart.getTotalOrderPrice());
	        order.setQuantity(cart.getQuantity());
	        order.setUser(cart.getUser());
	        order.setStatus(OrderStatus.IN_PROGRESS.name());
	        order.setPaymentType(orderRequestDto.getPaymentType());
	        

	        order.setMasterAddress(address); // ✅ assign selected address
	       lastorder=  productOrderRepository.save(order);
	    }
		return  lastorder;
	}

	@Override
	public List<ProductOrder> getAllProductOrders() {
		List<ProductOrder> all = productOrderRepository.findAll();
		return all;
	}
	
	@Override
	public Boolean updateOrderStatus(Integer id, String status) {
		Optional<ProductOrder> byId = productOrderRepository.findById(id);
		
		if(byId.isPresent()) {
			ProductOrder productOrder = byId.get();
			productOrder.setStatus(status);
			productOrderRepository.save(productOrder);
			return true;
		}
		return false;
	}

    @Override
    public ProductOrder getOrderById(Integer id) {
        return productOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

	@Override
	public ProductOrder getOrderByOrderId(Integer id) {
 return productOrderRepository.findByOrderId(id);
	
	}

	@Override
	public List<ProductOrder> getOrdersBetweenDates(LocalDate start, LocalDate end) {
		List<ProductOrder> byOrderDateBetween = productOrderRepository.findByOrderDateBetween(start, end);
		return byOrderDateBetween;
	}
    



}
	