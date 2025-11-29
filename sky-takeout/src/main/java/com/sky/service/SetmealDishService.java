package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {

    /**
     * 根据套餐ID获取关联菜品列表
     */
    List<SetmealDish> listBySetmealId(Long setmealId);

    /**
     * 批量保存套餐菜品关联关系
     */
    boolean saveBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐ID删除关联菜品
     */
    boolean removeBySetmealId(Long setmealId);
}
