package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.ShopDTO;
import com.sky.entity.Shop;
import com.sky.service.ShopService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 获取店铺信息
     */
    @GetMapping
    // @PreAuthorize("hasAuthority('shop:query')")
    public Result<Shop> getShopInfo() {
        Shop shop = shopService.getCurrentShop();
        return Result.success(shop);
    }
    
    /**
     * 根据ID获取店铺详情
     */
    @GetMapping("/{id}")
    public Result<Shop> getShopById(@PathVariable Long id) {
        Shop shop = shopService.getById(id);
        return Result.success(shop);
    }

    /**
     * 获取店铺列表（支持搜索）
     */
    @GetMapping("/list")
    public Result<List<Shop>> getShopList(@RequestParam(required = false) String name, 
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false) Long categoryId,
                                         @RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size) {
        List<Shop> shops = shopService.getShopList(name, status);
        return Result.success(shops);
    }

    /**
     * 获取店铺分页列表（支持搜索）
     */
    @GetMapping("/page")
    public Result<IPage<Shop>> getShopPage(@RequestParam(defaultValue = "1") Integer pageNum, 
                                          @RequestParam(defaultValue = "10") Integer pageSize, 
                                          @RequestParam(required = false) String name, 
                                          @RequestParam(required = false) Integer status) {
        Page<Shop> pageInfo = new Page<>(pageNum, pageSize);
        IPage<Shop> shopPage = shopService.getShopPage(pageInfo, name, status);
        return Result.success(shopPage);
    }

    /**
     * 更新店铺信息
     */
    @PutMapping
    @PreAuthorize("hasAuthority('shop:update')")
    public Result<Void> updateShop(@RequestBody ShopDTO shopDTO) {
        Shop shop = new Shop();
        shop.setId(shopDTO.getId());
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
        shop.setPhone(shopDTO.getPhone());
        shop.setDescription(shopDTO.getDescription());
        shop.setLogo(shopDTO.getLogo());
        shop.setOpeningHours(shopDTO.getOpeningHours());
        shop.setDeliveryFee(shopDTO.getDeliveryFee());
        shop.setDeliveryTime(shopDTO.getDeliveryTime());
        shop.setMinOrderAmount(shopDTO.getMinOrderAmount());
        shopService.updateById(shop);
        return Result.success();
    }

    /**
     * 更新店铺状态
     */
    @PutMapping("/status")
    // @PreAuthorize("hasAuthority('shop:update')")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        shopService.updateStatus(id, status);
        return Result.success();
    }

}