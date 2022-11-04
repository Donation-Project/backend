package com.donation.common.response.auth;

import com.donation.service.auth.domain.AuthToken;
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


    public static AccessAndRefreshTokenResponse of(AuthToken authToken) {
        return AccessAndRefreshTokenResponse.builder()
                .accessToken(authToken.getAccessToken())
                .refreshToken(authToken.getRefreshToken())
                .build();
    }
}