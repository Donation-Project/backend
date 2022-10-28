package com.donation.domain;

import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

public class UserTest {

    @Test
    @DisplayName("회원을 생성한다.")
    void 회원을_생성한다() {
        //given & when & then
        Assertions.assertDoesNotThrow(() -> createUser());
    }

    @ParameterizedTest
    @DisplayName("이메일 형식과 맞지 않으면 예외를 던진다.")
    @ValueSource(strings = {"default@", "default.email.com", "default.email","default@email", "@email.com"})
    void 이메일_형식과_맞지_않으면_예외를_던진다(final String email) {
        // given & when & then
        assertThatThrownBy(() -> createUser(email))
                .isInstanceOf(DonationInvalidateException.class);
    }

    @ParameterizedTest
    @DisplayName("회원의 패스워드를 변경한다.")
    @ValueSource(strings = {"newPassword"})
    void 회원의_패스워드를_변경한다(final String password){
        //given
        User user = createUser();

        //when
        user.changeNewPassword(password);

        //then
        assertThat(user.getPassword()).isEqualTo(password);
    }

    @ParameterizedTest
    @DisplayName("회원의 프로필 이미지를 변경한다.")
    @ValueSource(strings = {"newProfileImage"})
    void 회원의_프로필_이미지를_변경한다(final String profileImage){
        //given
        User user = createUser();

        //when
        user.changeNewProfileImage(profileImage);

        //then
        assertThat(user.getProfileImage()).isEqualTo(profileImage);
    }
}
