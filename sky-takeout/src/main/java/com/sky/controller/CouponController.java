package com.sky.controller;

import com.sky.entity.Coupon;
import com.sky.service.CouponService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    /**
     * 获取所有可用优惠券
     * @return 可用优惠券列表
     */
    @GetMapping("/available")
    public Result<List<Coupon>> getAvailableCoupons() {
        List<Coupon> coupons = couponService.getAvailableCoupons();
        return Result.success(coupons);
    }

    /**
     * 根据ID获取优惠券详情
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    @GetMapping("/{id}")
    public Result<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return Result.success(coupon);
    }
}