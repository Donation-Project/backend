package com.donation.common.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class LoginReqDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
