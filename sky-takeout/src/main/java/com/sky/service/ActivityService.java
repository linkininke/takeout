package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Activity;

import java.util.List;

public interface ActivityService extends IService<Activity> {
    /**
     * 获取所有可用活动
     * @return 活动列表
     */
    List<Activity> getAvailableActivities();

    /**
     * 根据ID获取活动详情
     * @param id 活动ID
     * @return 活动详情
     */
    Activity getActivityById(Long id);
}