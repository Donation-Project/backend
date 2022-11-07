package com.donation.domain;

import com.donation.exception.DonationInvalidateException;
import com.donation.service.auth.domain.AuthToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class AuthTokenTest {


    @Test
    @DisplayName("같은 리프레시 토큰 값이면 정상적으로 메서드를 종료한다.")
    void 같은_리프레시_토큰_값이면_정상적으로_메서드를_종료한다() {
        // given
        AuthToken authToken = new AuthToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        authToken.validateHasSameRefreshToken(authToken.getRefreshToken());
    }

    @DisplayName("같은 리프레시 토큰 값이 아니면 예외를 발생한다.")
    @Test
    void 같은_리프레시_토큰_값이_아니면_예외를_발생한다() {
        // given
        AuthToken authToken = new AuthToken("dummyAccessToken", "dummyRefreshToken");

        // when & then
        assertThatThrownBy(() -> authToken.validateHasSameRefreshToken("invalidRefreshToken"))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("회원의 리프레시 토큰이 아닙니다.");
    }
}
