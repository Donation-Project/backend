package com.donation.common.request.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @Builder
    public UserLoginReqDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
