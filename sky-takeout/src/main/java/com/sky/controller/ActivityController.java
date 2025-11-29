package com.sky.controller;

import com.sky.entity.Activity;
import com.sky.service.ActivityService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    /**
     * 获取所有可用活动
     * @return 可用活动列表
     */
    @GetMapping("/available")
    public Result<List<Activity>> getAvailableActivities() {
        List<Activity> activities = activityService.getAvailableActivities();
        return Result.success(activities);
    }

    /**
     * 根据ID获取活动详情
     * @param id 活动ID
     * @return 活动详情
     */
    @GetMapping("/{id}")
    public Result<Activity> getActivityById(@PathVariable Long id) {
        Activity activity = activityService.getActivityById(id);
        return Result.success(activity);
    }
}