package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.PointMallItem;

import java.util.List;

public interface PointMallItemService extends IService<PointMallItem> {
    /**
     * 获取所有可用的积分商城商品
     * @return 积分商城商品列表
     */
    List<PointMallItem> getAvailableItems();

    /**
     * 根据ID获取积分商城商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    PointMallItem getItemById(Long id);

    /**
     * 更新商品库存
     * @param id 商品ID
     * @param count 更新数量（正数为增加，负数为减少）
     * @return 更新结果
     */
    boolean updateStock(Long id, Integer count);
}