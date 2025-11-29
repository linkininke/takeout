package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.PointMallItem;
import com.sky.mapper.PointMallItemMapper;
import com.sky.service.PointMallItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointMallItemServiceImpl extends ServiceImpl<PointMallItemMapper, PointMallItem> implements PointMallItemService {
    @Override
    public List<PointMallItem> getAvailableItems() {
        LambdaQueryWrapper<PointMallItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointMallItem::getStatus, 1) // 1: 可用
                .gt(PointMallItem::getStock, 0); // 库存充足
        return list(queryWrapper);
    }

    @Override
    public PointMallItem getItemById(Long id) {
        return getById(id);
    }

    @Override
    public boolean updateStock(Long id, Integer count) {
        PointMallItem item = getById(id);
        if (item != null) {
            int newStock = item.getStock() + count;
            if (newStock >= 0) {
                item.setStock(newStock);
                return updateById(item);
            }
        }
        return false;
    }
}