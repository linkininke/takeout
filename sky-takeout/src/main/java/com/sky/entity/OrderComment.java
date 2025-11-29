package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order_comment")
public class OrderComment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long customerId;
    private Integer rating;
    private String content;
    private String images;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}