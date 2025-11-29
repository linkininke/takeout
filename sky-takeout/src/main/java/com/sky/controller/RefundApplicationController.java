package com.sky.controller;

import com.sky.entity.RefundApplication;
import com.sky.service.RefundApplicationService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refund-application")
public class RefundApplicationController {
    @Autowired
    private RefundApplicationService refundApplicationService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据订单ID获取退款申请
     * @param orderId 订单ID
     * @return 退款申请
     */
    @GetMapping("/order/{orderId}")
    public Result<RefundApplication> getRefundByOrderId(@PathVariable Long orderId) {
        RefundApplication refundApplication = refundApplicationService.getByOrderId(orderId);
        return Result.success(refundApplication);
    }

    /**
     * 获取当前登录顾客的退款申请列表
     * @return 退款申请列表
     */
    @GetMapping("/customer")
    public Result<List<RefundApplication>> getRefundByCustomer() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<RefundApplication> refundApplications = refundApplicationService.getByCustomerId(customerId);
        return Result.success(refundApplications);
    }

    /**
     * 获取所有退款申请列表（管理员使用）
     * @return 退款申请列表
     */
    @GetMapping("/all")
    public Result<List<RefundApplication>> getAllRefundApplications() {
        List<RefundApplication> refundApplications = refundApplicationService.getAllRefundApplications();
        return Result.success(refundApplications);
    }

    /**
     * 创建退款申请
     * @param refundApplication 退款申请信息
     * @return 创建结果
     */
    @PostMapping
    public Result<RefundApplication> createRefundApplication(@RequestBody RefundApplication refundApplication) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        refundApplication.setCustomerId(customerId);
        refundApplication.setStatus(0); // 0: 待处理
        refundApplicationService.createRefundApplication(refundApplication);
        return Result.success(refundApplication);
    }

    /**
     * 更新退款申请状态（管理员使用）
     * @param id 退款申请ID
     * @param status 退款状态
     * @param handleContent 处理内容
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<RefundApplication> updateRefundStatus(@PathVariable Long id, 
                                                      @RequestParam Integer status, 
                                                      @RequestParam String handleContent) {
        refundApplicationService.updateRefundStatus(id, status, handleContent);
        RefundApplication refundApplication = refundApplicationService.getById(id);
        return Result.success(refundApplication);
    }
}