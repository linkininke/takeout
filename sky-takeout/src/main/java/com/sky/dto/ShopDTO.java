package com.sky.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopDTO {

    private Long id;

    private String name;

    private String address;

    private String phone;

    private String description;

    private String logo;

    private String openingHours;

    private BigDecimal deliveryFee;

    private Integer deliveryTime;

    private BigDecimal minOrderAmount;

}