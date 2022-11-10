package com.donation.domain.auth.dto;

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