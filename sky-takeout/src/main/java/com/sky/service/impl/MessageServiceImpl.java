package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.entity.Message;
import com.sky.mapper.MessageMapper;
import com.sky.service.MessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> getByCustomerId(Long customerId) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getCustomerId, customerId);
        queryWrapper.orderByDesc(Message::getCreateTime);
        return list(queryWrapper);
    }

    @Override
    public Integer getUnreadCount(Long customerId) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getCustomerId, customerId);
        queryWrapper.eq(Message::getStatus, 0); // 0: 未读
        return (int) (long) count(queryWrapper);
    }

    @Override
    public boolean sendMessage(Long customerId, Integer type, String title, String content) {
        Message message = new Message();
        message.setCustomerId(customerId);
        message.setType(type);
        message.setTitle(title);
        message.setContent(content);
        message.setStatus(0); // 0: 未读
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        return save(message);
    }

    @Override
    public boolean markAsRead(Long id) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Message::getId, id)
                    .set(Message::getStatus, 1) // 1: 已读
                    .set(Message::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }

    @Override
    public boolean markAllAsRead(Long customerId) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Message::getCustomerId, customerId);
        updateWrapper.set(Message::getStatus, 1); // 1: 已读
        updateWrapper.set(Message::getUpdateTime, LocalDateTime.now());
        return update(updateWrapper);
    }
}
