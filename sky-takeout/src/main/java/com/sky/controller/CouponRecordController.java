package com.sky.controller;

import com.sky.entity.CouponRecord;
import com.sky.service.CouponRecordService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon-record")
public class CouponRecordController {
    @Autowired
    private CouponRecordService couponRecordService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 领取优惠券
     * @param couponId 优惠券ID
     * @return 领取结果
     */
    @PostMapping("/receive/{couponId}")
    public Result<String> receiveCoupon(@PathVariable Long couponId) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        boolean result = couponRecordService.receiveCoupon(couponId, customerId);
        if (result) {
            return Result.success("优惠券领取成功");
        } else {
            return Result.error("优惠券领取失败");
        }
    }

    /**
     * 获取当前登录顾客的优惠券领取记录
     * @return 优惠券领取记录列表
     */
    @GetMapping
    public Result<List<CouponRecord>> getCouponRecords() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<CouponRecord> couponRecords = couponRecordService.getByCustomerId(customerId);
        return Result.success(couponRecords);
    }

    /**
     * 使用优惠券
     * @param recordId 优惠券领取记录ID
     * @return 使用结果
     */
    @PutMapping("/{recordId}/use")
    public Result<String> useCoupon(@PathVariable Long recordId) {
        boolean result = couponRecordService.useCoupon(recordId);
        if (result) {
            return Result.success("优惠券使用成功");
        } else {
            return Result.error("优惠券使用失败");
        }
    }
}