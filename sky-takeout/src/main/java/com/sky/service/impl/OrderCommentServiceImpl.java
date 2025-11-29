package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.OrderComment;
import com.sky.mapper.OrderCommentMapper;
import com.sky.service.OrderCommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements OrderCommentService {
    @Override
    public OrderComment getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComment::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public List<OrderComment> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<OrderComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComment::getCustomerId, customerId);
        queryWrapper.orderByDesc(OrderComment::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public boolean createComment(OrderComment orderComment) {
        return save(orderComment);
    }

    @Override
    public boolean updateComment(OrderComment orderComment) {
        return updateById(orderComment);
    }

    @Override
    public boolean deleteComment(Long id) {
        return removeById(id);
    }
}