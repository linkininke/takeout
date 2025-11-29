package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.PointExchangeRecord;
import com.sky.entity.PointMallItem;
import com.sky.mapper.PointExchangeRecordMapper;
import com.sky.service.PointExchangeRecordService;
import com.sky.service.PointMallItemService;
import com.sky.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointExchangeRecordServiceImpl extends ServiceImpl<PointExchangeRecordMapper, PointExchangeRecord> implements PointExchangeRecordService {
    @Autowired
    private PointMallItemService pointMallItemService;

    @Autowired
    private PointService pointService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exchangeItem(Long customerId, Long itemId, String address, String phone, String consignee) {
        // 1. 检查商品是否存在且可用
        PointMallItem item = pointMallItemService.getItemById(itemId);
        if (item == null || item.getStatus() != 1 || item.getStock() <= 0) {
            return false;
        }

        // 2. 检查用户积分是否充足
        Integer pointBalance = pointService.getPointBalance(customerId);
        if (pointBalance < item.getPointPrice()) {
            return false;
        }

        // 3. 创建积分兑换记录
        PointExchangeRecord exchangeRecord = new PointExchangeRecord();
        exchangeRecord.setCustomerId(customerId);
        exchangeRecord.setItemId(itemId);
        exchangeRecord.setPointPrice(item.getPointPrice());
        exchangeRecord.setStatus(0); // 0: 待处理
        exchangeRecord.setAddress(address);
        exchangeRecord.setPhone(phone);
        exchangeRecord.setConsignee(consignee);
        exchangeRecord.setCreateTime(LocalDateTime.now());
        exchangeRecord.setUpdateTime(LocalDateTime.now());

        // 4. 保存兑换记录
        boolean saveResult = save(exchangeRecord);
        if (!saveResult) {
            return false;
        }

        // 5. 扣除用户积分
        boolean reducePointResult = pointService.reducePoints(customerId, item.getPointPrice(), "积分兑换商品：" + item.getName());
        if (!reducePointResult) {
            throw new RuntimeException("扣除积分失败");
        }

        // 6. 减少商品库存
        boolean updateStockResult = pointMallItemService.updateStock(itemId, -1);
        if (!updateStockResult) {
            throw new RuntimeException("更新商品库存失败");
        }

        return true;
    }

    @Override
    public List<PointExchangeRecord> getExchangeRecords(Long customerId) {
        LambdaQueryWrapper<PointExchangeRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointExchangeRecord::getCustomerId, customerId)
                .orderByDesc(PointExchangeRecord::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public boolean updateExchangeStatus(Long id, Integer status) {
        PointExchangeRecord exchangeRecord = new PointExchangeRecord();
        exchangeRecord.setId(id);
        exchangeRecord.setStatus(status);
        exchangeRecord.setUpdateTime(LocalDateTime.now());
        return updateById(exchangeRecord);
    }
}