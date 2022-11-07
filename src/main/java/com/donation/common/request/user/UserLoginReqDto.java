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

    @Builder
    public UserLoginReqDto(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
