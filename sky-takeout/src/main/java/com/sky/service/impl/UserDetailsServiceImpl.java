package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 如果密码不是BCrypt哈希值（不以$2a$开头），就自动转换为BCrypt哈希值
        String password = user.getPassword();
        if (!password.startsWith("$2a$")) {
            password = PasswordUtil.encode(password);
            user.setPassword(password);
            userMapper.updateById(user);
        }

        // 暂时返回固定权限列表，方便测试
        List<String> permissions = new ArrayList<>();
        permissions.add("user:list");
        permissions.add("user:query");
        permissions.add("user:add");
        permissions.add("user:update");
        permissions.add("user:delete");
        permissions.add("shop:query");
        permissions.add("shop:update");

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(password)
                .authorities(permissions.toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(user.getStatus() == 0)
                .build();
    }

}