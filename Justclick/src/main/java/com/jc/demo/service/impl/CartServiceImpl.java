package com.jc.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.jc.demo.model.Cart;
import com.jc.demo.model.Product;
import com.jc.demo.model.User;
import com.jc.demo.repository.CartRepository;
import com.jc.demo.repository.ProductRepository;
import com.jc.demo.repository.UserRepository;
import com.jc.demo.service.CartService;
@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepoditory;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;

	@Override
	public Cart saveCart(Integer productId, Integer userId) {
		User userDetails = userRepository.findById(userId).get();
	Product productDetails = productRepository.findById(productId).get();
	
	Cart cartStatus = cartRepoditory.findByProductIdAndUserId(productId, userId);
	
	Cart cart = null;
	//not in the cart:
	if(ObjectUtils.isEmpty(cartStatus)) {
		cart = new Cart();
		cart.setProduct(productDetails);
		cart.setUser(userDetails);
		cart.setQuantity(1);
		cart.setTotalPrice(1*productDetails.getDiscountprice());
		// already in the cart:
	}else {
		cart=cartStatus;
		cart.setQuantity(cart.getQuantity()+1);
		cart.setTotalPrice(cart.getQuantity()*cart.getProduct().getDiscountprice());
		
	}
		Cart save = cartRepoditory.save(cart);
		return save;
	}

	@Override
	public List<Cart> getCartsByUser(Integer userId) {
	    List<Cart> carts = cartRepoditory.findByUserId(userId);

	    Double totalOrderPrice = 0.0;
	    
	    List<Cart> updateCarts = new ArrayList<>();
	    
	    for (Cart c : carts) {
	        Double totalPrice = c.getProduct().getDiscountprice() * c.getQuantity();
	        c.setTotalPrice(totalPrice);
	        
	        totalOrderPrice += totalPrice;
	        c.setTotalOrderPrice(totalOrderPrice);

	        updateCarts.add(c);
	    }

	    return updateCarts;
	}

	@Override
	public Integer getCountCart(Integer userId) {
	Integer countByUserId=	cartRepoditory.countByUserId(userId);
		return countByUserId;
	}

	@Override
	public void updateQuantity(String sy, Integer cid ) {
		
	    Cart cart = cartRepoditory.findById(cid).get();

	    int updateQuantity; 
	    if (sy.equalsIgnoreCase("de")) {
	        updateQuantity = cart.getQuantity() - 1;

	        if (updateQuantity <= 0) {
	            cartRepoditory.delete(cart);
	        } else {
	            cart.setQuantity(updateQuantity);
	            cartRepoditory.save(cart);
	           
	        }
	    } else {
	        updateQuantity = cart.getQuantity() + 1;

	        if (updateQuantity > 5) {
	            updateQuantity = 5;
	        
	        }

	        cart.setQuantity(updateQuantity);
	        cartRepoditory.save(cart);
	    }
	}

//	@Override
//	public void clearCartByUserId(Integer userId) {
//		Optional<Cart> byId = cartRepoditory.findById(userId);
//		cartRepoditory.deleteById(byId);
//		
//	}
	
	@Override
	public void clearCartByUserId(Integer userId) {
	List<Cart> byUserId = cartRepoditory.findByUserId(userId);
	    cartRepoditory.deleteAll(byUserId);
	}


	
}
