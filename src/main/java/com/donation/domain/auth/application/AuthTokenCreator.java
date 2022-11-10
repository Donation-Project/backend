package com.donation.domain.auth.application;


import com.donation.domain.auth.entity.AuthToken;
import com.donation.domain.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthTokenCreator implements TokenCreator{

    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    public AuthToken createAuthToken(Long memberId) {
        String accessToken = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshToken = createRefreshToken(memberId);
        return new AuthToken(accessToken, refreshToken);
    }

    private String createRefreshToken(final Long memberId) {
        if (tokenRepository.exist(memberId)) {
            return tokenRepository.getToken(memberId);
        }
        String refreshToken = tokenProvider.createRefreshToken(String.valueOf(memberId));
        return tokenRepository.save(memberId, refreshToken);
    }

    @Override
    public AuthToken renewAuthToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        Long memberId = Long.valueOf(tokenProvider.getPayload(refreshToken));

        String accessTokenForRenew = tokenProvider.createAccessToken(String.valueOf(memberId));
        String refreshTokenForRenew = tokenRepository.getToken(memberId);

        AuthToken renewalAuthToken = new AuthToken(accessTokenForRenew, refreshTokenForRenew);
        renewalAuthToken.validateHasSameRefreshToken(refreshToken);
        return renewalAuthToken;
    }

    @Override
    public Long extractPayload(String accessToken) {
        tokenProvider.validateToken(accessToken);
        return Long.valueOf(tokenProvider.getPayload(accessToken));
    }
}
