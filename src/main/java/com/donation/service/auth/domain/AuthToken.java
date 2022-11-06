package com.donation.service.auth.domain;

import com.donation.exception.DonationInvalidateException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthToken {
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthToken(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void validateHasSameRefreshToken(final String otherRefreshToken) {
        if (!refreshToken.equals(otherRefreshToken)) {
            throw new DonationInvalidateException("회원의 리프레시 토큰이 아닙니다.");
        }
    }
}
