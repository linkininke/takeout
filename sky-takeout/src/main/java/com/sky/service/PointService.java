package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Point;

import java.util.List;

public interface PointService extends IService<Point> {
    /**
     * 获取顾客积分余额
     * @param customerId 顾客ID
     * @return 积分余额
     */
    Integer getPointBalance(Long customerId);

    /**
     * 增加积分
     * @param customerId 顾客ID
     * @param amount 积分数量
     * @param description 积分描述
     * @return 增加结果
     */
    boolean addPoints(Long customerId, Integer amount, String description);

    /**
     * 减少积分
     * @param customerId 顾客ID
     * @param amount 积分数量
     * @param description 积分描述
     * @return 减少结果
     */
    boolean reducePoints(Long customerId, Integer amount, String description);

    /**
     * 获取顾客积分记录
     * @param customerId 顾客ID
     * @return 积分记录列表
     */
    List<Point> getPointRecords(Long customerId);
}