package com.sky.controller;

import com.sky.entity.PointExchangeRecord;
import com.sky.service.PointExchangeRecordService;
import com.sky.utils.CurrentUserUtil;
import com.sky.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/point-exchange-record")
public class PointExchangeRecordController {
    @Autowired
    private PointExchangeRecordService pointExchangeRecordService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Autowired
    private HttpServletRequest request;

    /**
     * 积分兑换商品
     * @param itemId 商品ID
     * @param address 收货地址
     * @param phone 联系电话
     * @param consignee 收货人
     * @return 兑换结果
     */
    @PostMapping("/exchange")
    public Result<String> exchangeItem(@RequestParam Long itemId,
                                     @RequestParam String address,
                                     @RequestParam String phone,
                                     @RequestParam String consignee) {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        boolean result = pointExchangeRecordService.exchangeItem(customerId, itemId, address, phone, consignee);
        if (result) {
            return Result.success("积分兑换成功");
        } else {
            return Result.error("积分兑换失败");
        }
    }

    /**
     * 获取当前登录顾客的积分兑换记录
     * @return 积分兑换记录列表
     */
    @GetMapping
    public Result<List<PointExchangeRecord>> getExchangeRecords() {
        Long customerId = currentUserUtil.getCurrentCustomerId(request);
        List<PointExchangeRecord> records = pointExchangeRecordService.getExchangeRecords(customerId);
        return Result.success(records);
    }

    /**
     * 更新积分兑换记录状态（管理员使用）
     * @param id 积分兑换记录ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateExchangeStatus(@PathVariable Long id, @RequestParam Integer status) {
        boolean result = pointExchangeRecordService.updateExchangeStatus(id, status);
        if (result) {
            return Result.success("积分兑换记录状态更新成功");
        } else {
            return Result.error("积分兑换记录状态更新失败");
        }
    }
}