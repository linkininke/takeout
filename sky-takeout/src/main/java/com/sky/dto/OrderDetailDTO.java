package com.sky.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailDTO {
    private Long id;
    private Long orderId;
    private Long dishId;
    private Long setmealId;
    private String name;
    private String image;
    private BigDecimal amount;
    private Integer copies;
    private String dishFlavor;
}