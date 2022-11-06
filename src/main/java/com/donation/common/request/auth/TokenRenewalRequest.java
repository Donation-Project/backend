package com.donation.common.request.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class TokenRenewalRequest {
    @NotNull(message = "리프레시 토큰은 공백일 수 없습니다.")
    private String refreshToken;

    public TokenRenewalRequest(final String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
