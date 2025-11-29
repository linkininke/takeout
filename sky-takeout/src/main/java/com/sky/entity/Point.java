package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("point")
public class Point {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Integer type;
    private Integer amount;
    private String description;
    private LocalDateTime createTime;
}