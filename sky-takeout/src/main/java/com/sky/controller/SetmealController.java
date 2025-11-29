package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.service.SetmealService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 获取套餐分页列表
     */
    @GetMapping("/page")
    // @PreAuthorize("hasAuthority('setmeal:query')")
    public Result<IPage<Setmeal>> getSetmealPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) Long categoryId,
                                               @RequestParam(required = false) Integer status,
                                               @RequestParam(required = false) Long shopId) {
        Page<Setmeal> pageInfo = new Page<>(pageNum, pageSize);
        IPage<Setmeal> setmealPage = setmealService.page(pageInfo);
        return Result.success(setmealPage);
    }

    /**
     * 获取套餐详情
     */
    @GetMapping("/{id}")
    // @PreAuthorize("hasAuthority('setmeal:query')")
    public Result<SetmealDTO> getSetmealDetail(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);
        List<SetmealDish> setmealDishes = setmealService.getSetmealDishesBySetmealId(id);
        
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setId(setmeal.getId());
        setmealDTO.setName(setmeal.getName());
        setmealDTO.setCategoryId(setmeal.getCategoryId());
        setmealDTO.setPrice(setmeal.getPrice());
        setmealDTO.setImage(setmeal.getImage());
        setmealDTO.setDescription(setmeal.getDescription());
        setmealDTO.setStatus(setmeal.getStatus());
        setmealDTO.setSort(setmeal.getSort());
        setmealDTO.setShopId(setmeal.getShopId());
        setmealDTO.setSetmealDishes(setmealDishes);
        
        return Result.success(setmealDTO);
    }

    /**
     * 创建套餐
     */
    @PostMapping
    // @PreAuthorize("hasAuthority('setmeal:create')")
    public Result<Void> createSetmeal(@RequestBody SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        setmeal.setName(setmealDTO.getName());
        setmeal.setCategoryId(setmealDTO.getCategoryId());
        setmeal.setPrice(setmealDTO.getPrice());
        setmeal.setImage(setmealDTO.getImage());
        setmeal.setDescription(setmealDTO.getDescription());
        setmeal.setStatus(setmealDTO.getStatus());
        setmeal.setSort(setmealDTO.getSort());
        setmeal.setShopId(setmealDTO.getShopId());
        
        setmealService.saveSetmealWithDishes(setmeal, setmealDTO.getSetmealDishes());
        return Result.success();
    }

    /**
     * 更新套餐
     */
    @PutMapping("/{id}")
    // @PreAuthorize("hasAuthority('setmeal:update')")
    public Result<Void> updateSetmeal(@PathVariable Long id, @RequestBody SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setName(setmealDTO.getName());
        setmeal.setCategoryId(setmealDTO.getCategoryId());
        setmeal.setPrice(setmealDTO.getPrice());
        setmeal.setImage(setmealDTO.getImage());
        setmeal.setDescription(setmealDTO.getDescription());
        setmeal.setStatus(setmealDTO.getStatus());
        setmeal.setSort(setmealDTO.getSort());
        setmeal.setShopId(setmealDTO.getShopId());
        
        setmealService.updateSetmealWithDishes(setmeal, setmealDTO.getSetmealDishes());
        return Result.success();
    }

    /**
     * 删除套餐
     */
    @DeleteMapping("/{id}")
    // @PreAuthorize("hasAuthority('setmeal:delete')")
    public Result<Void> deleteSetmeal(@PathVariable Long id) {
        setmealService.removeSetmealWithDishes(id);
        return Result.success();
    }

    /**
     * 更新套餐状态
     */
    @PutMapping("/status")
    // @PreAuthorize("hasAuthority('setmeal:update')")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        setmealService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据分类ID获取套餐列表
     */
    @GetMapping("/category/{categoryId}")
    // @PreAuthorize("hasAuthority('setmeal:query')")
    public Result<List<Setmeal>> getSetmealsByCategoryId(@PathVariable Long categoryId, @RequestParam(required = false) Long shopId) {
        List<Setmeal> setmeals = setmealService.listByCategoryId(categoryId, shopId);
        return Result.success(setmeals);
    }

    /**
     * 获取所有套餐列表
     */
    @GetMapping("/list")
    // @PreAuthorize("hasAuthority('setmeal:query')")
    public Result<List<Setmeal>> getAllSetmeals(@RequestParam(required = false) Long shopId) {
        List<Setmeal> setmeals = setmealService.list();
        return Result.success(setmeals);
    }
}
