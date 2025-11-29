package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 店铺状态变更日志
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shop_status_log")
public class ShopStatusLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 变更前状态：1-营业中，0-休息中
     */
    private Integer oldStatus;

    /**
     * 变更后状态：1-营业中，0-休息中
     */
    private Integer newStatus;

    /**
     * 操作人员ID
     */
    private Long operatorId;

    /**
     * 操作人员名称
     */
    private String operatorName;

    /**
     * 变更时间
     */
    private LocalDateTime changeTime;

    /**
     * 备注
     */
    private String remark;

}