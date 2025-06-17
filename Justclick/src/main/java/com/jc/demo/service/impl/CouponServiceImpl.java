package com.jc.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.demo.model.Coupon;
import com.jc.demo.repository.CouponRepositor;
import com.jc.demo.service.CouponService;
@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private CouponRepositor couponRepositor;

	@Override
	public boolean saveCoupon(Coupon coupon) {
		Coupon save = couponRepositor.save(coupon);
		return true;
	}

	@Override
	public List<Coupon> getALLCoupon() {
		List<Coupon> all = couponRepositor.findAll();
		return all;
	}

	@Override
	public boolean deleteCoupon(Integer id) {
couponRepositor.deleteById(id);
		return true;
	}


}
