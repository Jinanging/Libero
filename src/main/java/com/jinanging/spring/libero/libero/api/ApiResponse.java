package com.jinanging.spring.libero.libero.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private String result;     
    private int code;           
    private String message;     
    private T data;             

    // 성공 응답 생성 메서드 (데이터 포함)
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .result("success")
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    // 실패 응답 생성 메서드 (ResponseCode enum 사용)
    public static <T> ApiResponse<T> fail(ResponseCode code) {
        return ApiResponse.<T>builder()
                .result("fail")
                .code(code.getCode())
                .message(code.getMessage())
                .data(null)
                .build();
    }

 
}
