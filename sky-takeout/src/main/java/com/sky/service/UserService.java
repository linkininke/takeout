package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.User;

import com.sky.dto.PasswordDTO;

public interface UserService extends IService<User> {

    User findByUsername(String username);
    
    void updatePassword(PasswordDTO passwordDTO);

}