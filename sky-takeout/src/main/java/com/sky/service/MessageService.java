package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    /**
     * 根据顾客ID获取消息列表
     * @param customerId 顾客ID
     * @return 消息列表
     */
    List<Message> getByCustomerId(Long customerId);

    /**
     * 根据顾客ID获取未读消息数量
     * @param customerId 顾客ID
     * @return 未读消息数量
     */
    Integer getUnreadCount(Long customerId);

    /**
     * 发送消息给顾客
     * @param customerId 顾客ID
     * @param type 消息类型
     * @param title 消息标题
     * @param content 消息内容
     * @return 发送结果
     */
    boolean sendMessage(Long customerId, Integer type, String title, String content);

    /**
     * 标记消息为已读
     * @param id 消息ID
     * @return 标记结果
     */
    boolean markAsRead(Long id);

    /**
     * 标记所有消息为已读
     * @param customerId 顾客ID
     * @return 标记结果
     */
    boolean markAllAsRead(Long customerId);
}
