package com.sky.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishDTO {

    private Long id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String description;

    private Integer status;

    private Integer sort;

    private Long shopId;

}