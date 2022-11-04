package com.donation.auth.application;


import com.donation.auth.domain.AuthToken;

public interface TokenCreator {

    AuthToken createAuthToken(final Long memberId);

    AuthToken renewAuthToken(final String refreshToken);

    Long extractPayload(final String accessToken);
}
