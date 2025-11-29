package com.sky.dto;

import com.sky.entity.SetmealDish;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SetmealDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private BigDecimal price;
    private String image;
    private String description;
    private Integer status;
    private Integer sort;
    private Long shopId;
    private List<SetmealDish> setmealDishes;
}
