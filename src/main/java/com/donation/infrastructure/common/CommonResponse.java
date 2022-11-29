package com.donation.infrastructure.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private boolean success;
    private T data;
    private ErrorResponse error;

    public static CommonResponse success() {
        return new CommonResponse(true, null, null);
    }

    public static <T>CommonResponse success(T data) {
        return new CommonResponse(true, data, null);
    }

    public static CommonResponse fail(ErrorResponse errorResponse) {
        return new CommonResponse(false, null, errorResponse);
    }
}