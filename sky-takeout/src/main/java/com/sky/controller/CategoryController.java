package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Category;
import com.sky.service.CategoryService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Category>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer type,
                                       @RequestParam(required = false) Long shopId) {
        Page<Category> page = new Page<>(pageNum, pageSize);
        IPage<Category> categoryPage = categoryService.page(page);
        return Result.success(categoryPage);
    }

    /**
     * 根据ID查询分类
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    /**
     * 创建分类
     */
    @PostMapping
    public Result<Void> create(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.updateById(category);
        return Result.success();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success();
    }

    /**
     * 根据类型获取分类列表
     */
    @GetMapping("/type/{type}")
    public Result<List<Category>> getByType(@PathVariable Integer type, @RequestParam Long shopId) {
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }

    /**
     * 获取所有分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(required = false) Long shopId) {
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }
}
