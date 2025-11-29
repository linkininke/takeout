package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 根据订单ID获取订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);

    /**
     * 批量保存订单明细
     * @param orderDetails 订单明细列表
     */
    void saveBatchOrderDetails(List<OrderDetail> orderDetails);
}