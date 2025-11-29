package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {

    List<Category> listByType(Integer type, Long shopId);

    boolean updateStatus(Long id, Integer status);

}