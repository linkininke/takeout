package com.sky.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WxUtil {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx.login-url}")
    private String loginUrl;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 调用微信登录接口获取openid
     * @param code 微信登录code
     * @return openid
     */
    public String getOpenid(String code) {
        String url = loginUrl + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String response = restTemplate.getForObject(url, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("openid").asText();
        } catch (Exception e) {
            throw new RuntimeException("解析微信登录响应失败", e);
        }
    }
}