package com.jc.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc.demo.model.Coupon;

public interface CouponRepositor extends JpaRepository<Coupon, Integer> {

}
