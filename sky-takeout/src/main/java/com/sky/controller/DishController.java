package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.service.DishService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 获取菜品分页列表
     */
    @GetMapping("/page")
    // @PreAuthorize("hasAuthority('dish:query')")
    public Result<IPage<Dish>> getDishPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) String name,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) Integer status,
                                           @RequestParam(required = false) Long shopId) {
        Page<Dish> pageInfo = new Page<>(pageNum, pageSize);
        IPage<Dish> dishPage = dishService.page(pageInfo, name, categoryId, status, shopId);
        return Result.success(dishPage);
    }

    /**
     * 获取菜品详情
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('dish:query')")
    public Result<Dish> getDishDetail(@PathVariable Long id) {
        Dish dish = dishService.getById(id);
        return Result.success(dish);
    }

    /**
     * 创建菜品
     */
    @PostMapping
    // @PreAuthorize("hasAuthority('dish:create')")
    public Result<Void> createDish(@RequestBody DishDTO dishDTO) {
        Dish dish = new Dish();
        dish.setName(dishDTO.getName());
        dish.setCategoryId(dishDTO.getCategoryId());
        dish.setPrice(dishDTO.getPrice());
        dish.setImage(dishDTO.getImage());
        dish.setDescription(dishDTO.getDescription());
        dish.setStatus(dishDTO.getStatus());
        dish.setSort(dishDTO.getSort());
        dish.setShopId(dishDTO.getShopId());
        dishService.save(dish);
        return Result.success();
    }

    /**
     * 更新菜品
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('dish:update')")
    public Result<Void> updateDish(@PathVariable Long id, @RequestBody DishDTO dishDTO) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setName(dishDTO.getName());
        dish.setCategoryId(dishDTO.getCategoryId());
        dish.setPrice(dishDTO.getPrice());
        dish.setImage(dishDTO.getImage());
        dish.setDescription(dishDTO.getDescription());
        dish.setStatus(dishDTO.getStatus());
        dish.setSort(dishDTO.getSort());
        dish.setShopId(dishDTO.getShopId());
        dishService.updateById(dish);
        return Result.success();
    }

    /**
     * 删除菜品
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('dish:delete')")
    public Result<Void> deleteDish(@PathVariable Long id) {
        dishService.removeById(id);
        return Result.success();
    }

    /**
     * 更新菜品状态
     */
    @PutMapping("/status")
    // @PreAuthorize("hasAuthority('dish:update')")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        dishService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据分类ID获取菜品列表
     */
    @GetMapping("/category/{categoryId}")
    // @PreAuthorize("hasAuthority('dish:query')")
    public Result<List<Dish>> getDishesByCategoryId(@PathVariable Long categoryId, @RequestParam(required = false) Long shopId) {
        List<Dish> dishes = dishService.listByCategoryId(categoryId, shopId);
        return Result.success(dishes);
    }

    /**
     * 获取所有菜品列表
     */
    @GetMapping("/list")
    // @PreAuthorize("hasAuthority('dish:query')")
    public Result<List<Dish>> getAllDishes(@RequestParam(required = false) Long shopId) {
        List<Dish> dishes = dishService.list();
        return Result.success(dishes);
    }
}
