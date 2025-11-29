package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.CouponRecord;

import java.util.List;

public interface CouponRecordService extends IService<CouponRecord> {
    /**
     * 领取优惠券
     * @param couponId 优惠券ID
     * @param customerId 顾客ID
     * @return 领取结果
     */
    boolean receiveCoupon(Long couponId, Long customerId);

    /**
     * 根据顾客ID获取优惠券领取记录
     * @param customerId 顾客ID
     * @return 优惠券领取记录列表
     */
    List<CouponRecord> getByCustomerId(Long customerId);

    /**
     * 使用优惠券
     * @param recordId 优惠券领取记录ID
     * @return 使用结果
     */
    boolean useCoupon(Long recordId);

    /**
     * 根据ID获取优惠券领取记录
     * @param id 优惠券领取记录ID
     * @return 优惠券领取记录
     */
    CouponRecord getById(Long id);
}