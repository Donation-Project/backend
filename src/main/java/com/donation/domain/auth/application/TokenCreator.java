package com.donation.domain.auth.application;


import com.donation.domain.auth.entity.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final Long memberId);

    AuthToken renewAuthToken(final String refreshToken);

    Long extractPayload(final String accessToken);
}
