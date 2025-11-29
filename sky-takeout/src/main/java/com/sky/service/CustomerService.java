package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Customer;

public interface CustomerService extends IService<Customer> {
    /**
     * 根据openid获取顾客信息
     * @param openid 微信openid
     * @return 顾客信息
     */
    Customer getByOpenid(String openid);
}