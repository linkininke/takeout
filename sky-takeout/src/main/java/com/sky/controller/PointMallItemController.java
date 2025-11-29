package com.sky.controller;

import com.sky.entity.PointMallItem;
import com.sky.service.PointMallItemService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/point-mall-item")
public class PointMallItemController {
    @Autowired
    private PointMallItemService pointMallItemService;

    /**
     * 获取所有可用的积分商城商品
     * @return 积分商城商品列表
     */
    @GetMapping("/available")
    public Result<List<PointMallItem>> getAvailableItems() {
        List<PointMallItem> items = pointMallItemService.getAvailableItems();
        return Result.success(items);
    }

    /**
     * 根据ID获取积分商城商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    public Result<PointMallItem> getItemById(@PathVariable Long id) {
        PointMallItem item = pointMallItemService.getItemById(id);
        return Result.success(item);
    }
}