package com.donation.common.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessAndRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    public AccessAndRefreshTokenResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}