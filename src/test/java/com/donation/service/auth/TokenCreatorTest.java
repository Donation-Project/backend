package com.donation.service.auth;

import com.donation.service.auth.application.TokenCreator;
import com.donation.service.auth.domain.AuthToken;
import com.donation.common.utils.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenCreatorTest extends ServiceTest {

    @Autowired
    private TokenCreator tokenCreator;

    @Test
    @DisplayName("엑세스 토큰과 리프레시 토큰을 발급한다.")
    void 엑세스_토큰과_리프레시_토큰을_발급한다() {
        // given
        Long memberId = 1L;

        // when
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // then
        assertThat(authToken.getAccessToken()).isNotEmpty();
        assertThat(authToken.getRefreshToken()).isNotEmpty();
    }

    @Test
    @DisplayName("리프레시 토큰으로 엑세스 토큰을 발급받는다.")
    void 리프레시_토큰으로_엑세스_토큰을_발급한다() {
        // given
        Long memberId = 1L;
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // when
        AuthToken actual = tokenCreator.renewAuthToken(authToken.getRefreshToken());

        // then
        assertThat(actual.getAccessToken()).isNotEmpty();
        assertThat(actual.getRefreshToken()).isNotEmpty();
    }

    @DisplayName("토큰에서 페이로드를 추출한다.")
    @Test
    void 토큰에서_페이로드를_추출한다() {
        // given
        Long memberId = 1L;
        AuthToken authToken = tokenCreator.createAuthToken(memberId);

        // when
        Long actual = tokenCreator.extractPayload(authToken.getAccessToken());

        // then
        assertThat(actual).isEqualTo(memberId);
    }
}
