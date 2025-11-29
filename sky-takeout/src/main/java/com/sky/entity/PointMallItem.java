package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("point_mall_item")
public class PointMallItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String image;
    private Integer pointPrice;
    private Integer stock;
    private Integer status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}