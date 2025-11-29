package com.sky.controller;

import com.sky.entity.OrderComment;
import com.sky.service.OrderCommentService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order-comment")
public class OrderCommentController {
    @Autowired
    private OrderCommentService orderCommentService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据订单ID获取评价信息
     * @param orderId 订单ID
     * @return 评价信息
     */
    @GetMapping("/order/{orderId}")
    public Result<OrderComment> getCommentByOrderId(@PathVariable Long orderId) {
        OrderComment comment = orderCommentService.getByOrderId(orderId);
        return Result.success(comment);
    }

    /**
     * 获取当前登录顾客的评价列表
     * @return 评价列表
     */
    @GetMapping("/customer")
    public Result<List<OrderComment>> getCommentByCustomer() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<OrderComment> comments = orderCommentService.getByCustomerId(customerId);
        return Result.success(comments);
    }

    /**
     * 创建订单评价
     * @param comment 评价信息
     * @return 创建结果
     */
    @PostMapping
    public Result<OrderComment> createComment(@RequestBody OrderComment comment) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        comment.setCustomerId(customerId);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        orderCommentService.createComment(comment);
        return Result.success(comment);
    }

    /**
     * 更新订单评价
     * @param id 评价ID
     * @param comment 评价信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<OrderComment> updateComment(@PathVariable Long id, @RequestBody OrderComment comment) {
        comment.setId(id);
        comment.setUpdateTime(LocalDateTime.now());
        orderCommentService.updateComment(comment);
        return Result.success(comment);
    }

    /**
     * 删除订单评价
     * @param id 评价ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        orderCommentService.deleteComment(id);
        return Result.success();
    }
}