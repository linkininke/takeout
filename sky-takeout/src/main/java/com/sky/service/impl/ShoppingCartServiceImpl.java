package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public List<ShoppingCart> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getCustomerId, customerId);
        return list(queryWrapper);
    }

    @Override
    public void clearByCustomerId(Long customerId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getCustomerId, customerId);
        remove(queryWrapper);
    }

    @Override
    public boolean updateNumber(Long id, Integer number) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(id);
        shoppingCart.setNumber(number);
        return this.updateById(shoppingCart);
    }

    @Override
    public boolean removeById(Long id) {
        return super.removeById(id);
    }

    @Override
    public ShoppingCart getByCustomerIdAndDishOrSetmealId(Long customerId, Long dishId, Long setmealId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getCustomerId, customerId);
        
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else if (setmealId != null) {
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }
        
        return this.getOne(queryWrapper);
    }
}