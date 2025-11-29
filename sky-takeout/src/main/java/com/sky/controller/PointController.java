package com.sky.controller;

import com.sky.entity.Point;
import com.sky.service.PointService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/point")
public class PointController {
    @Autowired
    private PointService pointService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取当前登录顾客的积分余额
     * @return 积分余额
     */
    @GetMapping("/balance")
    public Result<Integer> getPointBalance() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        Integer balance = pointService.getPointBalance(customerId);
        return Result.success(balance);
    }

    /**
     * 获取当前登录顾客的积分记录
     * @return 积分记录列表
     */
    @GetMapping("/records")
    public Result<List<Point>> getPointRecords() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<Point> records = pointService.getPointRecords(customerId);
        return Result.success(records);
    }
}