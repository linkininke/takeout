package com.sky.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersDTO {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private Long shopId;
    private Long addressBookId;
    private String consignee;
    private String phone;
    private String address;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal paymentAmount;
    private Integer status;
    private Integer payStatus;
    private Integer payMethod;
    private String remark;
    private String cancelReason;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
}