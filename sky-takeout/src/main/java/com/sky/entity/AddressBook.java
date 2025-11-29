package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("address_book")
public class AddressBook {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String consignee;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detail;
    private String label;
    private Integer isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}