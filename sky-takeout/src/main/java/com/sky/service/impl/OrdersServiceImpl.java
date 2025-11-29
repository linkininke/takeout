package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.OrdersDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import com.sky.service.OrderDetailService;
import com.sky.service.OrdersService;
import com.sky.vo.OrderDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public Long createOrder(OrdersDTO ordersDTO) {
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersDTO, orders);
        // 生成订单号等逻辑
        save(orders);
        return orders.getId();
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(status);
        updateById(orders);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
        // 查询订单信息
        Orders order = getById(id);
        
        // 查询订单明细
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
        
        // 构建返回结果
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setOrder(order);
        orderDetailVO.setOrderDetails(orderDetails);
        
        return orderDetailVO;
    }

    @Override
    public IPage<Orders> getUserOrderPage(Page<Orders> page, Long customerId, Integer status) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getCustomerId, customerId);
        if (status != null) {
            queryWrapper.eq(Orders::getStatus, status);
        }
        queryWrapper.orderByDesc(Orders::getCreateTime);
        return this.page(page, queryWrapper);
    }
}