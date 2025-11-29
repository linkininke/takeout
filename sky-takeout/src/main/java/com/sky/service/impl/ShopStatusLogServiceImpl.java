package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.ShopStatusLog;
import com.sky.mapper.ShopStatusLogMapper;
import com.sky.service.ShopStatusLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 店铺状态变更日志ServiceImpl
 */
@Service
public class ShopStatusLogServiceImpl extends ServiceImpl<ShopStatusLogMapper, ShopStatusLog> implements ShopStatusLogService {

    @Autowired
    private ShopStatusLogMapper shopStatusLogMapper;

}