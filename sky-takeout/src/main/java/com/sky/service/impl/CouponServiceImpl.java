package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Coupon;
import com.sky.mapper.CouponMapper;
import com.sky.service.CouponService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {
    @Override
    public List<Coupon> getAvailableCoupons() {
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coupon::getStatus, 1) // 1: 可用
                .apply("total_count > used_count") // 库存充足
                .le(Coupon::getStartTime, LocalDateTime.now()) // 已开始
                .ge(Coupon::getEndTime, LocalDateTime.now()); // 未过期
        return list(queryWrapper);
    }

    @Override
    public Coupon getCouponById(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateUsedCount(Long id) {
        Coupon coupon = getById(id);
        if (coupon != null && coupon.getUsedCount() < coupon.getTotalCount()) {
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            return updateById(coupon);
        }
        return false;
    }
}