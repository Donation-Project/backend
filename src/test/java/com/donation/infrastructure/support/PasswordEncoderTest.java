package com.donation.infrastructure.support;

import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.donation.common.UserFixtures.일반_사용자_패스워드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordEncoderTest {
    private final AuthEncoder authEncoder = new AuthEncoder();

    @ParameterizedTest
    @DisplayName("패스워드 encode 성공")
    @ValueSource(strings = {"newPassword", "woojin"})
    void 패스워드_Encode_성공(final String Text) {
        //given & when
        boolean actual = authEncoder.compare(Text, authEncoder.encode(Text));

        //then
        assertThat(actual).isTrue();
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 텍스트 비교시 오류를 던진다")
    @ValueSource(strings = {"유효하지않은 텍스트"})
    void 유효하지_않은_텍스트_비교시_오류를_던진다(final String Text){
        //given & when & then
        assertThatThrownBy(() -> authEncoder.compare(Text, authEncoder.encode(일반_사용자_패스워드)))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("인증정보가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("텍스트 비교시 맞는 텍스트를 입력하면 true를 반환한다.")
    void 텍스트_비교시_맞는_텍스트를_입력하면_true를_반환한다(){
        //given & when
        boolean actual = authEncoder.compare(일반_사용자_패스워드, authEncoder.encode(일반_사용자_패스워드));

        //then
        assertThat(actual).isTrue();
    }

}