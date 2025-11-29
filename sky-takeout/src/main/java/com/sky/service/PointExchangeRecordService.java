package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.PointExchangeRecord;

import java.util.List;

public interface PointExchangeRecordService extends IService<PointExchangeRecord> {
    /**
     * 积分兑换商品
     * @param customerId 顾客ID
     * @param itemId 商品ID
     * @param address 收货地址
     * @param phone 联系电话
     * @param consignee 收货人
     * @return 兑换结果
     */
    boolean exchangeItem(Long customerId, Long itemId, String address, String phone, String consignee);

    /**
     * 获取顾客积分兑换记录
     * @param customerId 顾客ID
     * @return 积分兑换记录列表
     */
    List<PointExchangeRecord> getExchangeRecords(Long customerId);

    /**
     * 更新兑换记录状态
     * @param id 兑换记录ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateExchangeStatus(Long id, Integer status);
}