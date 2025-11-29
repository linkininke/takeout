package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Coupon;
import com.sky.entity.CouponRecord;
import com.sky.mapper.CouponRecordMapper;
import com.sky.service.CouponRecordService;
import com.sky.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponRecordServiceImpl extends ServiceImpl<CouponRecordMapper, CouponRecord> implements CouponRecordService {
    @Autowired
    private CouponService couponService;

    @Override
    public boolean receiveCoupon(Long couponId, Long customerId) {
        // 1. 检查优惠券是否存在且可用
        Coupon coupon = couponService.getCouponById(couponId);
        if (coupon == null || coupon.getStatus() != 1 || coupon.getUsedCount() >= coupon.getTotalCount()) {
            return false;
        }

        // 2. 检查用户是否已经领取过该优惠券
        LambdaQueryWrapper<CouponRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponRecord::getCouponId, couponId)
                .eq(CouponRecord::getCustomerId, customerId);
        if (count(queryWrapper) > 0) {
            return false;
        }

        // 3. 创建优惠券领取记录
        CouponRecord couponRecord = new CouponRecord();
        couponRecord.setCouponId(couponId);
        couponRecord.setCustomerId(customerId);
        couponRecord.setStatus(0); // 0: 未使用
        couponRecord.setReceiveTime(LocalDateTime.now());
        couponRecord.setExpireTime(coupon.getEndTime());
        couponRecord.setCreateTime(LocalDateTime.now());
        couponRecord.setUpdateTime(LocalDateTime.now());

        // 4. 保存领取记录
        boolean saveResult = save(couponRecord);
        if (saveResult) {
            // 5. 更新优惠券使用数量
            couponService.updateUsedCount(couponId);
        }

        return saveResult;
    }

    @Override
    public List<CouponRecord> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<CouponRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CouponRecord::getCustomerId, customerId);
        queryWrapper.orderByDesc(CouponRecord::getReceiveTime);
        return list(queryWrapper);
    }

    @Override
    public boolean useCoupon(Long recordId) {
        CouponRecord couponRecord = getById(recordId);
        if (couponRecord != null && couponRecord.getStatus() == 0) {
            couponRecord.setStatus(1); // 1: 已使用
            couponRecord.setUseTime(LocalDateTime.now());
            couponRecord.setUpdateTime(LocalDateTime.now());
            return updateById(couponRecord);
        }
        return false;
    }

    @Override
    public CouponRecord getById(Long id) {
        return super.getById(id);
    }
}