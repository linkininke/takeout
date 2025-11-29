package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("refund_application")
public class RefundApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;
    private Long customerId;
    private BigDecimal refundAmount;
    private Integer reasonType;
    private String reasonContent;
    private String images;
    private Integer status;
    private String handleContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime handleTime;
}