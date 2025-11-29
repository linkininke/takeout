package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.RefundApplication;
import com.sky.mapper.RefundApplicationMapper;
import com.sky.service.RefundApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefundApplicationServiceImpl extends ServiceImpl<RefundApplicationMapper, RefundApplication> implements RefundApplicationService {
    @Override
    public RefundApplication getByOrderId(Long orderId) {
        LambdaQueryWrapper<RefundApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundApplication::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public List<RefundApplication> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<RefundApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RefundApplication::getCustomerId, customerId);
        queryWrapper.orderByDesc(RefundApplication::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public boolean createRefundApplication(RefundApplication refundApplication) {
        refundApplication.setCreateTime(LocalDateTime.now());
        refundApplication.setUpdateTime(LocalDateTime.now());
        return save(refundApplication);
    }

    @Override
    public boolean updateRefundStatus(Long id, Integer status, String handleContent) {
        RefundApplication refundApplication = new RefundApplication();
        refundApplication.setId(id);
        refundApplication.setStatus(status);
        refundApplication.setHandleContent(handleContent);
        refundApplication.setHandleTime(LocalDateTime.now());
        refundApplication.setUpdateTime(LocalDateTime.now());
        return updateById(refundApplication);
    }

    @Override
    public List<RefundApplication> getAllRefundApplications() {
        LambdaQueryWrapper<RefundApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(RefundApplication::getCreateTime);
        return list(queryWrapper);
    }
}