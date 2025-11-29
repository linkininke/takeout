package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService extends IService<AddressBook> {
    /**
     * 根据顾客ID获取地址列表
     * @param customerId 顾客ID
     * @return 地址列表
     */
    List<AddressBook> getByCustomerId(Long customerId);

    /**
     * 获取默认地址
     * @param customerId 顾客ID
     * @return 默认地址
     */
    AddressBook getDefaultAddress(Long customerId);

    /**
     * 将顾客的所有地址设为非默认
     * @param customerId 顾客ID
     */
    void setNonDefault(Long customerId);
}