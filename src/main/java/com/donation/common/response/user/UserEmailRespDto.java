package com.donation.common.response.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEmailRespDto {

    private static final String AVAILABLE_EMAIL = "사용가능한 이메일입니다.";
    private static final String UNAVAILABLE_EMAIL = "이미 중복된 이메일이 존재합니다.";

    String message;

    public UserEmailRespDto(String message) {
        this.message = message;
    }

    public static UserEmailRespDto of(boolean exists){
        if(!exists)
            return new UserEmailRespDto(AVAILABLE_EMAIL);
        return new UserEmailRespDto(UNAVAILABLE_EMAIL);
    }
}
