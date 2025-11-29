package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.OrdersDTO;
import com.sky.entity.Orders;
import com.sky.service.OrdersService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.OrderDetailVO;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 创建订单
     * @param ordersDTO 订单数据传输对象
     * @return 订单ID
     */
    @PostMapping
    public Result<Long> createOrder(@RequestBody OrdersDTO ordersDTO) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        ordersDTO.setCustomerId(customerId);
        Long orderId = ordersService.createOrder(ordersDTO);
        return Result.success(orderId);
    }

    /**
     * 获取当前登录顾客的订单列表
     * @param page 页码
     * @param pageSize 每页大小
     * @param status 订单状态
     * @return 订单分页列表
     */
    @GetMapping("/user")
    public Result<IPage<Orders>> getUserOrderPage(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) Integer status) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        IPage<Orders> orderPage = ordersService.getUserOrderPage(pageInfo, customerId, status);
        return Result.success(orderPage);
    }

    /**
     * 获取订单列表
     * @param page 页码
     * @param pageSize 每页大小
     * @return 订单分页列表
     */
    @GetMapping("/page")
    public Result<IPage<Orders>> getOrderPage(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        IPage<Orders> orderPage = ordersService.page(pageInfo);
        return Result.success(orderPage);
    }

    /**
     * 获取订单详情
     * @param id 订单ID
     * @return 订单详情，包含订单明细
     */
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable Long id) {
        OrderDetailVO orderDetailVO = ordersService.getOrderDetail(id);
        return Result.success(orderDetailVO);
    }

    /**
     * 更新订单状态
     * @param id 订单ID
     * @param status 订单状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateOrderStatus(@PathVariable Long id,
                                            @RequestParam Integer status) {
        ordersService.updateOrderStatus(id, status);
        return Result.success("订单状态更新成功");
    }
}