package com.jc.demo.service;

import java.util.List;

import com.jc.demo.model.Coupon;

public interface CouponService {
	
	public boolean saveCoupon(Coupon coupon);
	public List<Coupon> getALLCoupon();
	public boolean deleteCoupon(Integer id);

}
