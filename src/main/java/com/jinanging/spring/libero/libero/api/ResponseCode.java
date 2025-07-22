package com.jinanging.spring.libero.libero.api;

public enum ResponseCode {
    SUCCESS(1000, "성공"),
    FAIL(2000,"실패"),
    LOGIN_FAILED(2001, "아이디 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATE_ID(2003, "중복된 아이디"),
    INVALID_PARAMETER(4000, "잘못된 요청 파라미터"),
    SERVER_ERROR(5000, "서버 오류");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
