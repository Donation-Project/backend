package com.donation.common.request.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserLoginReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String refreshToken;

    @Builder
    public UserLoginReqDto(final String email, final String password, final String refreshToken) {
        this.email = email;
        this.password = password;
        this.refreshToken = refreshToken;
    }
}
