package com.donation.infrastructure.support;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.common.PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.donation.common.UserFixtures.일반_사용자_패스워드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordEncoderTest {
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @ParameterizedTest
    @DisplayName("패스워드 encode 성공")
    @ValueSource(strings = {"newPassword", "woojin"})
    void 패스워드_Encode_성공(final String password) {
        //given & when
        boolean actual = passwordEncoder.compare(password, passwordEncoder.encode(password));

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 패스워드 비교시 오류를 던진다")
    @ValueSource(strings = {"유효하지않은 패스워드"})
    void 유효하지_않은_패스워드_비교시_오류를_던진다(final String password){
        //given & when & then
        assertThatThrownBy(() -> passwordEncoder.compare(password, passwordEncoder.encode(일반_사용자_패스워드)))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("패스워드가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("패스워드 비교시 맞는 패스워드를 입력하면 true를 반환한다.")
    void 패스워드_비교시_맞는_패스워드를_입력하면_true를_반환한다(){
        //given & when
        boolean actual = passwordEncoder.compare(일반_사용자_패스워드, passwordEncoder.encode(일반_사용자_패스워드));

        //then
        assertThat(actual).isTrue();
    }

}