package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    List<Dish> listByCategoryId(Long categoryId, Long shopId);

    boolean updateStatus(Long id, Integer status);

    IPage<Dish> page(Page<Dish> page, String name, Long categoryId, Integer status, Long shopId);

}