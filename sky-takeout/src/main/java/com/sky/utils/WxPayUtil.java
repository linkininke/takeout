package com.sky.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Base64;

@Component
public class WxPayUtil {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.pay.mch-id}")
    private String mchId;

    @Value("${wx.pay.mch-key}")
    private String mchKey;

    @Value("${wx.pay.notify-url}")
    private String notifyUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 微信统一下单
     * @param orderId 订单ID
     * @param totalAmount 总金额（单位：分）
     * @param body 商品描述
     * @param openid 用户openid
     * @return 统一下单结果
     */
    public Map<String, String> unifiedOrder(Long orderId, Integer totalAmount, String body, String openid) {
        // 构建请求参数
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", appid);
        params.put("mch_id", mchId);
        params.put("nonce_str", generateNonceStr());
        params.put("body", body);
        params.put("out_trade_no", String.valueOf(orderId));
        params.put("total_fee", String.valueOf(totalAmount));
        params.put("spbill_create_ip", "127.0.0.1");
        params.put("notify_url", notifyUrl);
        params.put("trade_type", "JSAPI");
        params.put("openid", openid);
        
        // 生成签名
        String sign = generateSign(params, mchKey);
        params.put("sign", sign);
        
        // 转换为XML格式
        String xmlParams = mapToXml(params);
        
        // 调用微信统一下单API
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String response = restTemplate.postForObject(url, xmlParams, String.class);
        
        // 将XML响应转换为Map
        Map<String, String> result = xmlToMap(response);
        
        return result;
    }

    /**
     * 生成JSAPI支付所需的参数
     * @param prepayId 预支付ID
     * @return JSAPI支付参数
     */
    public Map<String, String> generateJsApiParams(String prepayId) {
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appId", appid);
        params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("nonceStr", generateNonceStr());
        params.put("package", "prepay_id=" + prepayId);
        params.put("signType", "MD5");
        
        // 生成签名
        String sign = generateSign(params, mchKey);
        params.put("paySign", sign);
        
        return params;
    }

    /**
     * 处理微信支付回调
     * @param xmlData 回调XML数据
     * @return 处理结果
     */
    public Map<String, String> handlePayNotify(String xmlData) {
        // 将XML转换为Map
        Map<String, String> notifyMap = xmlToMap(xmlData);
        
        // 验证签名
        String sign = notifyMap.get("sign");
        notifyMap.remove("sign");
        String generatedSign = generateSign(notifyMap, mchKey);
        
        Map<String, String> result = new TreeMap<>();
        if (sign.equals(generatedSign)) {
            // 签名验证成功
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
        } else {
            // 签名验证失败
            result.put("return_code", "FAIL");
            result.put("return_msg", "签名验证失败");
        }
        
        return result;
    }

    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    private String generateNonceStr() {
        return String.valueOf(System.currentTimeMillis()) + (int)(Math.random() * 10000);
    }

    /**
     * 生成签名
     * @param params 参数Map
     * @param key 商户密钥
     * @return 签名
     */
    private String generateSign(Map<String, String> params, String key) {
        // 实现签名生成逻辑，这里使用MD5签名
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(entry.getKey()).append("=")
                  .append(entry.getValue()).append("&");
            }
        }
        sb.append("key=").append(key);
        return md5(sb.toString()).toUpperCase();
    }
    
    /**
     * MD5加密
     * @param str 待加密字符串
     * @return 加密结果
     */
    private String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * Map转换为XML
     * @param map Map数据
     * @return XML字符串
     */
    public String mapToXml(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">")
              .append(entry.getValue())
              .append("</").append(entry.getKey()).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * XML转换为Map
     * @param xml XML字符串
     * @return Map数据
     */
    private Map<String, String> xmlToMap(String xml) {
        // 这里使用简单的XML解析，实际项目中可以使用第三方库如Dom4j
        Map<String, String> map = new TreeMap<>();
        
        // 实现XML解析逻辑
        // 注意：这是一个简化的实现，实际项目中需要更健壮的XML解析
        
        return map;
    }
}