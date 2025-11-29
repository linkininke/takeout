package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.entity.RefundApplication;

import java.util.List;

public interface RefundApplicationService extends IService<RefundApplication> {
    /**
     * 根据订单ID获取退款申请
     * @param orderId 订单ID
     * @return 退款申请
     */
    RefundApplication getByOrderId(Long orderId);

    /**
     * 根据顾客ID获取退款申请列表
     * @param customerId 顾客ID
     * @return 退款申请列表
     */
    List<RefundApplication> getByCustomerId(Long customerId);

    /**
     * 创建退款申请
     * @param refundApplication 退款申请信息
     * @return 创建结果
     */
    boolean createRefundApplication(RefundApplication refundApplication);

    /**
     * 更新退款申请状态
     * @param id 退款申请ID
     * @param status 退款状态
     * @param handleContent 处理内容
     * @return 更新结果
     */
    boolean updateRefundStatus(Long id, Integer status, String handleContent);

    /**
     * 获取所有退款申请列表
     * @return 退款申请列表
     */
    List<RefundApplication> getAllRefundApplications();
}