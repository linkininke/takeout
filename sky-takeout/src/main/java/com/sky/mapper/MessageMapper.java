package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
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
}
