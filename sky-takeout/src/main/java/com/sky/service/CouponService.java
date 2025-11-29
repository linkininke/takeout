package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Coupon;

import java.util.List;

public interface CouponService extends IService<Coupon> {
    /**
     * 获取所有可用优惠券
     * @return 优惠券列表
     */
    List<Coupon> getAvailableCoupons();

    /**
     * 根据ID获取优惠券详情
     * @param id 优惠券ID
     * @return 优惠券详情
     */
    Coupon getCouponById(Long id);

    /**
     * 更新优惠券使用数量
     * @param id 优惠券ID
     * @return 更新结果
     */
    boolean updateUsedCount(Long id);
}