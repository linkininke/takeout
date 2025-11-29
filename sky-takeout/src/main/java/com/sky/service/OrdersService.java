package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.OrdersDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderDetailVO;

public interface OrdersService extends IService<Orders> {
    /**
     * 创建订单
     * @param ordersDTO 订单数据传输对象
     * @return 订单ID
     */
    Long createOrder(OrdersDTO ordersDTO);

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 订单状态
     */
    void updateOrderStatus(Long id, Integer status);

    /**
     * 获取订单详情
     * @param id 订单ID
     * @return 订单信息，包含订单明细
     */
    OrderDetailVO getOrderDetail(Long id);

    /**
     * 获取用户订单分页列表
     * @param page 分页信息
     * @param customerId 顾客ID
     * @param status 订单状态
     * @return 订单分页列表
     */
    IPage<Orders> getUserOrderPage(Page<Orders> page, Long customerId, Integer status);
}