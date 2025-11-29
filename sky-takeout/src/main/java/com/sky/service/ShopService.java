package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Shop;

import java.util.List;

public interface ShopService extends IService<Shop> {

    Shop getCurrentShop();

    boolean updateStatus(Long id, Integer status);

    List<Shop> getShopList(String name, Integer status);

    IPage<Shop> getShopPage(Page<Shop> pageInfo, String name, Integer status);

}