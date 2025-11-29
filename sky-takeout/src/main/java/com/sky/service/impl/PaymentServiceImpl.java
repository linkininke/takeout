package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Payment;
import com.sky.mapper.PaymentMapper;
import com.sky.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {
    @Override
    public Payment getByOrderId(Long orderId) {
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Payment::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public void updatePaymentStatus(Long orderId, Integer status) {
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Payment::getOrderId, orderId);
        Payment payment = getOne(queryWrapper);
        if (payment != null) {
            payment.setStatus(status);
            updateById(payment);
        }
    }

    @Override
    public boolean createPayment(Payment payment) {
        return this.save(payment);
    }

    @Override
    public boolean handlePaymentCallback(Payment payment) {
        // 这里需要验证支付回调的真实性，然后更新支付状态
        // 实际项目中需要根据第三方支付平台的要求进行验证
        this.updatePaymentStatus(payment.getOrderId(), payment.getStatus());
        return true;
    }

    @Override
    public boolean simulatePayment(Long orderId, Integer status) {
        // 这里只是模拟支付，实际项目中需要调用第三方支付平台API
        this.updatePaymentStatus(orderId, status);
        return true;
    }
}