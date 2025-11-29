package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealDishService;
import com.sky.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public IPage<Setmeal> page(Page<Setmeal> page, String name, Long categoryId, Integer status, Long shopId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        // 添加查询条件
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Setmeal::getName, name);
        }
        if (categoryId != null) {
            queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        }
        if (status != null) {
            queryWrapper.eq(Setmeal::getStatus, status);
        }
        if (shopId != null) {
            queryWrapper.eq(Setmeal::getShopId, shopId);
        }

        // 添加排序
        queryWrapper.orderByDesc(Setmeal::getCreateTime);

        return setmealMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Setmeal> listByCategoryId(Long categoryId, Long shopId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        queryWrapper.eq(Setmeal::getShopId, shopId);
        queryWrapper.eq(Setmeal::getStatus, 1);
        queryWrapper.orderByAsc(Setmeal::getSort);
        return setmealMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        return this.updateById(setmeal);
    }

    @Override
    @Transactional
    public boolean saveSetmealWithDishes(Setmeal setmeal, List<SetmealDish> setmealDishes) {
        // 保存套餐
        boolean saved = this.save(setmeal);
        if (!saved) {
            return false;
        }

        // 保存套餐菜品关联关系
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 设置套餐ID
            setmealDishes.forEach(dish -> dish.setSetmealId(setmeal.getId()));
            return setmealDishService.saveBatch(setmealDishes);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean updateSetmealWithDishes(Setmeal setmeal, List<SetmealDish> setmealDishes) {
        // 更新套餐
        boolean updated = this.updateById(setmeal);
        if (!updated) {
            return false;
        }

        // 删除原有的套餐菜品关联关系
        setmealDishService.removeBySetmealId(setmeal.getId());

        // 保存新的套餐菜品关联关系
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            // 设置套餐ID
            setmealDishes.forEach(dish -> dish.setSetmealId(setmeal.getId()));
            return setmealDishService.saveBatch(setmealDishes);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean removeSetmealWithDishes(Long id) {
        // 删除套餐
        boolean removed = this.removeById(id);
        if (!removed) {
            return false;
        }

        // 删除套餐菜品关联关系
        return setmealDishService.removeBySetmealId(id);
    }

    @Override
    public List<SetmealDish> getSetmealDishesBySetmealId(Long setmealId) {
        return setmealDishService.listBySetmealId(setmealId);
    }
}
