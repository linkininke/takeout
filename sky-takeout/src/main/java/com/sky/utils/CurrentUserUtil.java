package com.sky.utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CurrentUserUtil {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 从请求中获取当前登录顾客ID
     * @param request HTTP请求
     * @return 顾客ID
     */
    public Long getCurrentCustomerId(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
            Claims claims = jwtUtil.parseToken(token);
            return Long.parseLong(claims.get("customerId").toString());
        }
        return null;
    }

    /**
     * 从请求头中获取JWT令牌
     * @param request HTTP请求
     * @return JWT令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}