package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.OrderComment;

import java.util.List;

public interface OrderCommentService extends IService<OrderComment> {
    /**
     * 根据订单ID获取评价信息
     * @param orderId 订单ID
     * @return 评价信息
     */
    OrderComment getByOrderId(Long orderId);

    /**
     * 根据顾客ID获取评价列表
     * @param customerId 顾客ID
     * @return 评价列表
     */
    List<OrderComment> getByCustomerId(Long customerId);

    /**
     * 创建订单评价
     * @param orderComment 评价信息
     * @return 创建结果
     */
    boolean createComment(OrderComment orderComment);

    /**
     * 更新订单评价
     * @param orderComment 评价信息
     * @return 更新结果
     */
    boolean updateComment(OrderComment orderComment);

    /**
     * 删除订单评价
     * @param id 评价ID
     * @return 删除结果
     */
    boolean deleteComment(Long id);
}