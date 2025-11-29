package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 获取套餐分页列表
     */
    IPage<Setmeal> page(Page<Setmeal> page, String name, Long categoryId, Integer status, Long shopId);

    /**
     * 根据分类ID获取套餐列表
     */
    List<Setmeal> listByCategoryId(Long categoryId, Long shopId);

    /**
     * 更新套餐状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 保存套餐及关联菜品
     */
    boolean saveSetmealWithDishes(Setmeal setmeal, List<SetmealDish> setmealDishes);

    /**
     * 更新套餐及关联菜品
     */
    boolean updateSetmealWithDishes(Setmeal setmeal, List<SetmealDish> setmealDishes);

    /**
     * 删除套餐及关联菜品
     */
    boolean removeSetmealWithDishes(Long id);

    /**
     * 根据套餐ID获取关联菜品列表
     */
    List<SetmealDish> getSetmealDishesBySetmealId(Long setmealId);
}
