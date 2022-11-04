package com.donation.service.auth;

import com.donation.service.user.auth.application.JwtTokenProvider;
import com.donation.service.user.auth.application.TokenProvider;
import com.donation.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TokenProviderTest {

    private static final String JWT_SECRET_KEY = "A".repeat(32); // Secret Key는 최소 32바이트 이상이어야함.
    private static final int JWT_ACCESS_TOKEN_EXPIRE_LENGTH = 3600;
    private static final int JWT_REFRESH_TOKEN_EXPIRE_LENGTH = 3600;
    private static final String PAYLOAD = "payload";
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY,
            JWT_ACCESS_TOKEN_EXPIRE_LENGTH, JWT_REFRESH_TOKEN_EXPIRE_LENGTH);

    @Test
    @DisplayName("엑세스 토큰을 발급받는다.")
    void 엑세스_토큰을_발급받는다() {
        //given & when
        String actual = jwtTokenProvider.createAccessToken(PAYLOAD);

        // then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("토큰의 PAYLOAD를 가져온다.")
    @Test
    void 토큰의_PAYLOAD를_가져온다() {
        // given
        String token = jwtTokenProvider.createAccessToken(PAYLOAD);

        // when
        String actual = jwtTokenProvider.getPayload(token);

        // then
        assertThat(actual).isEqualTo(PAYLOAD);
    }

    @Test
    @DisplayName("엑세스 토큰을 검증하여 만료된 경우 예외를 던진다.")
    void 엑세스_토큰을_검증하여_만료된_경우_예외를_던진다() {
        // given
        TokenProvider expiredJwtTokenProvider = new JwtTokenProvider(JWT_SECRET_KEY, 0, 0);
        String expiredToken = expiredJwtTokenProvider.createAccessToken(PAYLOAD);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(DonationInvalidateException.class);
    }

    @Test
    @DisplayName("토큰을 검증하여 유효하지 않으면 예외를 던진다.")
    void 토큰을_검증하여_유효하지_않으면_예외를_던진다() {
        // given
        String malformedToken = "malformed";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(malformedToken))
                .isInstanceOf(DonationInvalidateException.class);
    }
}
