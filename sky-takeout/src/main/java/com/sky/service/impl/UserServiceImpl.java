package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.dto.PasswordDTO;
import com.sky.entity.User;
import com.sky.exception.BusinessException;
import com.sky.mapper.UserMapper;
import com.sky.service.UserService;
import com.sky.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO) {
        // 1. 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            throw new BusinessException("用户未登录");
        }
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 2. 根据用户名查询用户信息
        User user = findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 3. 验证旧密码是否正确
        if (!PasswordUtil.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        
        // 4. 验证新密码和确认密码是否一致
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            throw new BusinessException("两次输入的新密码不一致");
        }
        
        // 5. 加密新密码
        String encodedPassword = PasswordUtil.encode(passwordDTO.getNewPassword());
        
        // 6. 更新用户密码
        user.setPassword(encodedPassword);
        userMapper.updateById(user);
    }

}