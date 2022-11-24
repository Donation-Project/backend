package com.donation.domain.auth.entity;

import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.AuthFixtures.시리얼넘버;
import static com.donation.common.AuthFixtures.인증코드;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthCodeTest {

    @Test
    @DisplayName("인증 코드가 정상적으로 생성된다.")
    void 인증코드가_정상적으로_생성된다(){
        Assertions.assertDoesNotThrow(() -> AuthCode.of(인증코드, 시리얼넘버));
    }

    @Test
    @DisplayName("인증코드가 일치하지 않으면 오류를 던진다.")
    void 인증코드가_일치하지_않으면_오류를_던진다(){
        //given
        String expect = "failAuthCode";

        //when & then
        assertThatThrownBy(() -> 인증코드().verify(expect))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("인증 코드가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("인증코드가 일치시 로직을 통과한다.")
    void 인증코드가_일치시_로직을_통과한다(){
        Assertions.assertDoesNotThrow(() -> 인증코드().verify(인증코드));
    }
}