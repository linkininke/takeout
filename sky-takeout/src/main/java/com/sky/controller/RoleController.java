package com.sky.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.entity.Role;
import com.sky.entity.Permission;
import com.sky.service.RoleService;
import com.sky.service.PermissionService;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 角色列表分页查询
     */
    @GetMapping("/page")
    public Result<IPage<Role>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String name) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        IPage<Role> rolePage = roleService.page(page);
        return Result.success(rolePage);
    }

    /**
     * 根据ID查询角色
     */
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<Void> create(@RequestBody Role role) {
        roleService.save(role);
        return Result.success();
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Role role) {
        role.setId(id);
        roleService.updateById(role);
        return Result.success();
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.removeById(id);
        return Result.success();
    }

    /**
     * 获取角色的权限列表
     */
    @GetMapping("/{id}/permissions")
    public Result<List<Permission>> getRolePermissions(@PathVariable Long id) {
        // 这里需要实现获取角色权限的逻辑，暂时返回空列表
        return Result.success(List.of());
    }

    /**
     * 分配权限给角色
     */
    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody List<Long> permissionIds) {
        // 这里需要实现分配权限的逻辑
        return Result.success();
    }

    /**
     * 获取所有角色列表
     */
    @GetMapping
    public Result<List<Role>> list() {
        List<Role> roles = roleService.list();
        return Result.success(roles);
    }
}
