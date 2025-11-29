package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("point_exchange_record")
public class PointExchangeRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long itemId;
    private Integer pointPrice;
    private Integer status;
    private String address;
    private String phone;
    private String consignee;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}