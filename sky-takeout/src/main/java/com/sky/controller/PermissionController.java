package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Permission;
import com.sky.service.PermissionService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 权限列表分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Permission>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) Integer type) {
        Page<Permission> page = new Page<>(pageNum, pageSize);
        IPage<Permission> permissionPage = permissionService.page(page);
        return Result.success(permissionPage);
    }

    /**
     * 根据ID查询权限
     */
    @GetMapping("/{id}")
    public Result<Permission> getById(@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        return Result.success(permission);
    }

    /**
     * 创建权限
     */
    @PostMapping
    public Result<Void> create(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.success();
    }

    /**
     * 更新权限
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Permission permission) {
        permission.setId(id);
        permissionService.updateById(permission);
        return Result.success();
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        permissionService.removeById(id);
        return Result.success();
    }

    /**
     * 获取权限树
     */
    @GetMapping("/tree")
    public Result<List<Permission>> getPermissionTree() {
        // 这里需要实现获取权限树的逻辑，暂时返回空列表
        return Result.success(List.of());
    }

    /**
     * 获取所有权限列表
     */
    @GetMapping("/list")
    public Result<List<Permission>> list() {
        List<Permission> permissions = permissionService.list();
        return Result.success(permissions);
    }

    /**
     * 获取权限选项（用于下拉选择）
     */
    @GetMapping("/options")
    public Result<List<Permission>> getPermissionOptions() {
        List<Permission> permissions = permissionService.list();
        return Result.success(permissions);
    }
}
