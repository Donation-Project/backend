package com.donation.common.response.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccessTokenResponse {
    private String accessToken;

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}