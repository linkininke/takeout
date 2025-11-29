package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Shop;
import com.sky.entity.ShopStatusLog;
import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import com.sky.service.ShopStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

    @Autowired
    private ShopMapper shopMapper;
    
    @Autowired
    private ShopStatusLogService shopStatusLogService;

    @Override
    public Shop getCurrentShop() {
        // 这里假设当前系统只有一个店铺，实际项目中可能需要根据登录用户获取对应的店铺
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.last("LIMIT 1");
        Shop shop = shopMapper.selectOne(queryWrapper);
        
        // 如果没有店铺记录，创建一个默认的店铺记录
        if (shop == null) {
            shop = new Shop();
            shop.setName("苍穹外卖");
            shop.setAddress("默认地址");
            shop.setPhone("13800138000");
            shop.setDescription("默认店铺");
            shop.setStatus(1);
            shop.setDeliveryFee(new BigDecimal(0));
            shop.setDeliveryTime(30);
            shop.setMinOrderAmount(new BigDecimal(0));
            shopMapper.insert(shop);
        }
        
        return shop;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        // 获取当前店铺状态
        Shop shop = this.getById(id);
        if (shop == null) {
            return false;
        }
        
        Integer oldStatus = shop.getStatus();
        
        // 如果状态没有变化，直接返回成功
        if (oldStatus.equals(status)) {
            return true;
        }
        
        // 更新店铺状态
        shop.setStatus(status);
        boolean result = this.updateById(shop);
        
        // 记录状态变更日志
        if (result) {
            ShopStatusLog log = new ShopStatusLog();
            log.setShopId(id);
            log.setOldStatus(oldStatus);
            log.setNewStatus(status);
            
            // 从SecurityContext获取当前登录用户信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                // 这里假设用户名就是操作人员名称，实际项目中可能需要从数据库查询
                log.setOperatorName(username);
            } else {
                log.setOperatorName("系统管理员");
            }
            
            log.setChangeTime(LocalDateTime.now());
            log.setRemark("店铺状态从" + (oldStatus == 1 ? "营业中" : "休息中") + "变更为" + (status == 1 ? "营业中" : "休息中"));
            shopStatusLogService.save(log);
        }
        
        return result;
    }

    @Override
    public List<Shop> getShopList(String name, Integer status) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Shop::getName, name);
        }
        if (status != null) {
            queryWrapper.eq(Shop::getStatus, status);
        }
        queryWrapper.orderByDesc(Shop::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public IPage<Shop> getShopPage(Page<Shop> pageInfo, String name, Integer status) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(Shop::getName, name);
        }
        if (status != null) {
            queryWrapper.eq(Shop::getStatus, status);
        }
        queryWrapper.orderByDesc(Shop::getCreateTime);
        return this.page(pageInfo, queryWrapper);
    }

}