package com.sky.controller;

import com.sky.entity.Payment;
import com.sky.service.PaymentService;
import com.sky.utils.WxPayUtil;
import com.sky.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WxPayUtil wxPayUtil;

    /**
     * 根据订单ID获取支付信息
     * @param orderId 订单ID
     * @return 支付信息
     */
    @GetMapping("/order/{orderId}")
    public Result<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        Payment payment = paymentService.getByOrderId(orderId);
        return Result.success(payment);
    }

    /**
     * 创建支付记录
     * @param payment 支付信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Payment> createPayment(@RequestBody Payment payment) {
        paymentService.save(payment);
        return Result.success(payment);
    }

    /**
     * 微信支付统一下单
     * @param orderId 订单ID
     * @param openid 用户openid
     * @return 微信支付参数
     */
    @PostMapping("/wx-pay/{orderId}")
    public Result<Map<String, String>> wxPay(@PathVariable Long orderId, @RequestParam String openid) {
        // 1. 根据订单ID获取订单信息，计算总金额
        // 这里需要根据实际业务逻辑获取订单信息和总金额
        Integer totalAmount = 100; // 示例：1元
        String body = "苍穹外卖订单支付";
        
        // 2. 调用微信统一下单API
        Map<String, String> result = wxPayUtil.unifiedOrder(orderId, totalAmount, body, openid);
        
        // 3. 生成JSAPI支付所需的参数
        if ("SUCCESS".equals(result.get("return_code")) && "SUCCESS".equals(result.get("result_code"))) {
            String prepayId = result.get("prepay_id");
            Map<String, String> jsApiParams = wxPayUtil.generateJsApiParams(prepayId);
            return Result.success(jsApiParams);
        } else {
            return Result.error("微信支付统一下单失败：" + result.get("return_msg"));
        }
    }

    /**
     * 处理微信支付回调
     * @param xmlData 回调XML数据
     * @return 回调处理结果
     */
    @PostMapping("/wx-notify")
    public String handleWxPayNotify(@RequestBody String xmlData) {
        // 1. 处理微信支付回调
        Map<String, String> result = wxPayUtil.handlePayNotify(xmlData);
        
        // 2. 如果支付成功，更新订单状态和支付状态
        if ("SUCCESS".equals(result.get("return_code"))) {
            // 这里需要解析回调数据，获取订单ID和支付状态
            // 然后更新订单状态和支付状态
        }
        
        // 3. 返回微信要求的XML格式响应
        return wxPayUtil.mapToXml(result);
    }

    /**
     * 模拟支付
     * @param orderId 订单ID
     * @param status 支付状态
     * @return 支付结果
     */
    @PostMapping("/simulate/{orderId}")
    public Result<String> simulatePayment(@PathVariable Long orderId, @RequestParam Integer status) {
        // 这里只是模拟支付，实际项目中需要调用第三方支付平台API
        paymentService.updatePaymentStatus(orderId, status);
        return Result.success("支付状态已更新");
    }

    /**
     * 处理支付回调
     * @param payment 支付回调信息
     * @return 回调处理结果
     */
    @PostMapping("/callback")
    public Result<String> handlePaymentCallback(@RequestBody Payment payment) {
        // 这里需要验证支付回调的真实性，然后更新支付状态
        paymentService.updatePaymentStatus(payment.getOrderId(), payment.getStatus());
        return Result.success("回调处理成功");
    }
}
