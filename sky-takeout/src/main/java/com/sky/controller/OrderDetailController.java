package com.sky.controller;

import com.sky.entity.OrderDetail;
import com.sky.service.OrderDetailService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 根据订单ID获取订单明细
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    @GetMapping("/order/{orderId}")
    public Result<List<OrderDetail>> getOrderDetailsByOrderId(@PathVariable Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        return Result.success(orderDetails);
    }
}