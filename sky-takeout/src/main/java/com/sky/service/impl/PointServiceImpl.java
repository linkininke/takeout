package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Point;
import com.sky.mapper.PointMapper;
import com.sky.service.PointService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {
    @Override
    public Integer getPointBalance(Long customerId) {
        return baseMapper.getPointBalance(customerId);
    }

    @Override
    public boolean addPoints(Long customerId, Integer amount, String description) {
        Point point = new Point();
        point.setCustomerId(customerId);
        point.setType(1); // 1: 增加积分
        point.setAmount(amount);
        point.setDescription(description);
        point.setCreateTime(LocalDateTime.now());
        return save(point);
    }

    @Override
    public boolean reducePoints(Long customerId, Integer amount, String description) {
        // 检查积分余额是否充足
        Integer balance = getPointBalance(customerId);
        if (balance < amount) {
            return false;
        }

        Point point = new Point();
        point.setCustomerId(customerId);
        point.setType(2); // 2: 减少积分
        point.setAmount(amount);
        point.setDescription(description);
        point.setCreateTime(LocalDateTime.now());
        return save(point);
    }

    @Override
    public List<Point> getPointRecords(Long customerId) {
        LambdaQueryWrapper<Point> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Point::getCustomerId, customerId)
                .orderByDesc(Point::getCreateTime);
        return list(queryWrapper);
    }
}