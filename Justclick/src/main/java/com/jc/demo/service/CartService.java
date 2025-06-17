package com.jc.demo.service;

import java.util.List;

import com.jc.demo.model.Cart;

public interface CartService {
	
	public Cart saveCart(Integer productId ,Integer userId);
	
	public List<Cart> getCartsByUser(Integer UserId);
	
	public Integer getCountCart(Integer userId);
    
	public void updateQuantity(String sy, Integer cid);
	
	void clearCartByUserId(Integer userId);

}

//th:href="{'/user/adtocart?pid='+ ${product.id} + '&uid=' + ${usern.id}}"