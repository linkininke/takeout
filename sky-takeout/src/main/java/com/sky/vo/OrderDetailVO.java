package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailVO {
    private Orders order;
    private List<OrderDetail> orderDetails;
}