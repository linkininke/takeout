package com.sky.controller;

import com.sky.entity.ShoppingCart;
import com.sky.service.ShoppingCartService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取当前登录顾客的购物车列表
     * @return 购物车列表
     */
    @GetMapping
    public Result<List<ShoppingCart>> getShoppingCart() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<ShoppingCart> shoppingCarts = shoppingCartService.getByCustomerId(customerId);
        return Result.success(shoppingCarts);
    }

    /**
     * 添加商品到购物车
     * @param shoppingCart 购物车商品信息
     * @return 添加结果
     */
    @PostMapping
    public Result<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        shoppingCart.setCustomerId(customerId);
        
        // 检查商品是否已存在于购物车
        ShoppingCart existingCart = shoppingCartService.getByCustomerIdAndDishOrSetmealId(
                customerId, 
                shoppingCart.getDishId(), 
                shoppingCart.getSetmealId());
        
        if (existingCart != null) {
            // 如果商品已存在，更新数量
            existingCart.setNumber(existingCart.getNumber() + shoppingCart.getNumber());
            shoppingCartService.updateById(existingCart);
            return Result.success(existingCart);
        } else {
            // 如果商品不存在，添加到购物车
            shoppingCartService.save(shoppingCart);
            return Result.success(shoppingCart);
        }
    }

    /**
     * 更新购物车商品数量
     * @param id 购物车ID
     * @param number 商品数量
     * @return 更新结果
     */
    @PutMapping("/{id}/number")
    public Result<Void> updateShoppingCartNumber(@PathVariable Long id, @RequestParam Integer number) {
        shoppingCartService.updateNumber(id, number);
        return Result.success();
    }

    /**
     * 删除单个购物车商品
     * @param id 购物车ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteShoppingCartItem(@PathVariable Long id) {
        shoppingCartService.removeById(id);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return 清空结果
     */
    @DeleteMapping
    public Result<String> clearShoppingCart() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        shoppingCartService.clearByCustomerId(customerId);
        return Result.success("购物车已清空");
    }
}