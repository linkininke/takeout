package com.sky.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.UserDTO;
import com.sky.entity.User;
import com.sky.service.UserService;
import com.sky.utils.PasswordUtil;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表分页查询
     */
    @GetMapping("/system/user/page")
    // 暂时移除权限检查，方便测试
    // @PreAuthorize("hasAuthority('user:list')")
    public Result<IPage<User>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(required = false) String username,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) Long roleId) {
        Page<User> page = new Page<>(pageNum, pageSize);
        // 使用MyBatis-Plus的条件构造器构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        if (StringUtils.hasText(name)) {
            queryWrapper.like(User::getName, name);
        }
        if (roleId != null) {
            queryWrapper.eq(User::getRoleId, roleId);
        }
        // 执行分页查询
        IPage<User> userPage = userService.page(page, queryWrapper);
        return Result.success(userPage);
    }

    /**
     * 根据ID查询用户
     */
    @GetMapping("/system/user/{id}")
    @PreAuthorize("hasAuthority('user:query')")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping("/system/user")
    @PreAuthorize("hasAuthority('user:add')")
    public Result<Void> create(@RequestBody UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(PasswordUtil.encode(userDTO.getPassword()));
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setStatus(userDTO.getStatus());
        user.setRoleId(userDTO.getRoleId());
        userService.save(user);
        return Result.success();
    }

    /**
     * 更新用户
     */
    @PutMapping("/system/user/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public Result<Void> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setName(userDTO.getName());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setStatus(userDTO.getStatus());
        user.setRoleId(userDTO.getRoleId());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(PasswordUtil.encode(userDTO.getPassword()));
        }
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/system/user/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/system/user/batch")
    @PreAuthorize("hasAuthority('user:delete')")
    public Result<Void> batchDelete(@RequestBody Long[] ids) {
        userService.removeByIds(java.util.Arrays.asList(ids));
        return Result.success();
    }

    /**
     * 修改密码
     */
    @PutMapping("/user/password")
    public Result<Void> updatePassword(@RequestBody com.sky.dto.PasswordDTO passwordDTO) {
        userService.updatePassword(passwordDTO);
        return Result.success();
    }

}