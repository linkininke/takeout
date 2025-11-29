package com.sky.controller;

import com.sky.entity.Message;
import com.sky.service.MessageService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取当前登录顾客的消息列表
     * @return 消息列表
     */
    @GetMapping
    public Result<List<Message>> getMessages() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<Message> messages = messageService.getByCustomerId(customerId);
        return Result.success(messages);
    }

    /**
     * 获取当前登录顾客的未读消息数量
     * @return 未读消息数量
     */
    @GetMapping("/unread-count")
    public Result<Integer> getUnreadCount() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        Integer unreadCount = messageService.getUnreadCount(customerId);
        return Result.success(unreadCount);
    }

    /**
     * 标记消息为已读
     * @param id 消息ID
     * @return 标记结果
     */
    @PutMapping("/{id}/read")
    public Result<String> markAsRead(@PathVariable Long id) {
        boolean result = messageService.markAsRead(id);
        if (result) {
            return Result.success("消息已标记为已读");
        } else {
            return Result.error("消息标记失败");
        }
    }

    /**
     * 标记所有消息为已读
     * @return 标记结果
     */
    @PutMapping("/read-all")
    public Result<String> markAllAsRead() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        boolean result = messageService.markAllAsRead(customerId);
        if (result) {
            return Result.success("所有消息已标记为已读");
        } else {
            return Result.error("消息标记失败");
        }
    }
}
