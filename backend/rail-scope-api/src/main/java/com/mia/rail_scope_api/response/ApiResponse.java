package com.mia.rail_scope_api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String errorMessage;

    // 成功用
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 失敗用
    public static <T> ApiResponse<T> fail(String errorMessage) {
        return new ApiResponse<>(false, null, errorMessage);
    }
}
