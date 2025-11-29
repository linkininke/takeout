package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Activity;
import com.sky.mapper.ActivityMapper;
import com.sky.service.ActivityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Override
    public List<Activity> getAvailableActivities() {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getStatus, 1) // 1: 可用
                .le(Activity::getStartTime, LocalDateTime.now()) // 已开始
                .ge(Activity::getEndTime, LocalDateTime.now()); // 未过期
        return list(queryWrapper);
    }

    @Override
    public Activity getActivityById(Long id) {
        return getById(id);
    }
}