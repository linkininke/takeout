package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    /**
     * 根据顾客ID获取购物车列表
     * @param customerId 顾客ID
     * @return 购物车列表
     */
    List<ShoppingCart> getByCustomerId(Long customerId);

    /**
     * 清空购物车
     * @param customerId 顾客ID
     */
    void clearByCustomerId(Long customerId);

    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param number 商品数量
     * @return 更新结果
     */
    boolean updateNumber(Long id, Integer number);

    /**
     * 删除单个购物车商品
     * @param id 购物车ID
     * @return 删除结果
     */
    boolean removeById(Long id);

    /**
     * 根据顾客ID和商品信息获取购物车商品
     * @param customerId 顾客ID
     * @param dishId 菜品ID
     * @param setmealId 套餐ID
     * @return 购物车商品
     */
    ShoppingCart getByCustomerIdAndDishOrSetmealId(Long customerId, Long dishId, Long setmealId);
}