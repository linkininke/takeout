package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Payment;

public interface PaymentService extends IService<Payment> {
    /**
     * 根据订单ID获取支付信息
     * @param orderId 订单ID
     * @return 支付信息
     */
    Payment getByOrderId(Long orderId);

    /**
     * 更新支付状态
     * @param orderId 订单ID
     * @param status 支付状态
     */
    void updatePaymentStatus(Long orderId, Integer status);

    /**
     * 创建支付记录
     * @param payment 支付信息
     * @return 创建结果
     */
    boolean createPayment(Payment payment);

    /**
     * 处理支付回调
     * @param payment 支付回调信息
     * @return 处理结果
     */
    boolean handlePaymentCallback(Payment payment);

    /**
     * 模拟支付
     * @param orderId 订单ID
     * @param status 支付状态
     * @return 模拟结果
     */
    boolean simulatePayment(Long orderId, Integer status);
}