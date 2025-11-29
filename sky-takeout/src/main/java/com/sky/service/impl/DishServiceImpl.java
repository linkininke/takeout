package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Dish> listByCategoryId(Long categoryId, Long shopId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getShopId, shopId);
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort);
        return dishMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        return this.updateById(dish);
    }

    @Override
    public IPage<Dish> page(Page<Dish> page, String name, Long categoryId, Integer status, Long shopId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Dish::getName, name);
        }
        if (categoryId != null) {
            queryWrapper.eq(Dish::getCategoryId, categoryId);
        }
        if (status != null) {
            queryWrapper.eq(Dish::getStatus, status);
        }
        if (shopId != null) {
            queryWrapper.eq(Dish::getShopId, shopId);
        }
        
        // 添加排序
        queryWrapper.orderByDesc(Dish::getCreateTime);
        
        return dishMapper.selectPage(page, queryWrapper);
    }

}