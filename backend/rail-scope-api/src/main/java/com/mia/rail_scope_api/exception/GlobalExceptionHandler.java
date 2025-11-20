package com.mia.rail_scope_api.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mia.rail_scope_api.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        // 這裡可以記 log
        // log.error("系統錯誤", ex);
        return ApiResponse.fail("系統發生錯誤：" + ex.getMessage());
    }
}
