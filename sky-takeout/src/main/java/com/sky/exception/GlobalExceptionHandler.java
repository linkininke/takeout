package com.sky.exception;

import com.sky.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        // 在开发环境下返回详细的错误信息
        return Result.error("系统异常：" + e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result handleUnauthorizedException(UnauthorizedException e) {
        log.error("未授权异常：{}", e.getMessage());
        return Result.error(401, "未授权，请重新登录");
    }

    @ExceptionHandler(ForbiddenException.class)
    public Result handleForbiddenException(ForbiddenException e) {
        log.error("禁止访问异常：{}", e.getMessage());
        return Result.error(403, "禁止访问，没有权限");
    }

}