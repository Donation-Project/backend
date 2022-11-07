package com.donation.service.auth;

import com.donation.common.request.auth.TokenRenewalRequest;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.common.response.auth.AccessAndRefreshTokenResponse;
import com.donation.common.response.auth.AccessTokenResponse;
import com.donation.common.utils.ServiceTest;
import com.donation.exception.DonationDuplicateException;
import com.donation.exception.DonationInvalidateException;
import com.donation.service.auth.application.AuthService;
import com.donation.service.auth.repository.TokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthServiceTest extends ServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void clear() {
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("토큰을 인증 후 반환")
    void 토큰을_인증_후_반환(){
        //given & when
        authService.save(유저_회원가입_DTO);
        AccessAndRefreshTokenResponse actual = authService.login(유저_로그인_DTO);

        // then
        Assertions.assertAll(() -> {
            assertThat(actual.getAccessToken()).isNotEmpty();
            assertThat(actual.getRefreshToken()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급한다.")
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급한다() {
        // given
        authService.save(new UserSaveReqDto(일반_사용자_이메일, 일반_사용자_이름, 일반_사용자_패스워드, 일반_사용자_메타마스크_주소));
        AccessAndRefreshTokenResponse response = authService.login(유저_로그인_DTO);
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest(response.getRefreshToken());

        // when
        AccessTokenResponse accessTokenResponse = authService.renewalToken(tokenRenewalRequest);

        // then
        assertThat(accessTokenResponse.getAccessToken()).isNotEmpty();
    }

    @Test
    @DisplayName("리프레시 토큰으로 새로운 엑세스 토큰을 발급 할 때, 리프레시 토큰이 존재하지 않으면 예외를 던진다.")
    void 리프레시_토큰으로_새로운_엑세스_토큰을_발급_할_때_리프레시_토큰이_존재하지_않으면_예외를_던진다() {
        // given
        TokenRenewalRequest tokenRenewalRequest = new TokenRenewalRequest("TempRefreshToken");

        // when & then
        assertThatThrownBy(() -> authService.renewalToken(tokenRenewalRequest))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("권한이 없습니다.");
    }

    @Test
    @DisplayName("유저 회원가입 성공")
    void 회원_회원가입_성공(){
        //given & when & then
        assertDoesNotThrow(() -> authService.save(유저_회원가입_DTO));
    }

    @Test
    @DisplayName("회원 회원가입시 중복된 정보로 인한 예외발생")
    void 회원_회원가입시_잘못된_중복된_인한_예외발생(){
        //given
        authService.save(유저_회원가입_DTO);

        //when & then
        assertThatThrownBy(() -> authService.save(유저_회원가입_DTO))
                .isInstanceOf(DonationDuplicateException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }


    @Test
    @DisplayName("유저 로그인 성공")
    void 유저_로그인_성공(){
        //given
        authService.save(new UserSaveReqDto(일반_사용자_이메일, 일반_사용자_이름, 일반_사용자_패스워드, 일반_사용자_메타마스크_주소));

        //when & then
        assertDoesNotThrow(() -> authService.login(유저_로그인_DTO));
    }

    @Test
    @DisplayName("다른 패스워드로 로그인 요청시 예외 발생")
    void 다른_패스워드로_로그인_요청시_예외_발생(){
        //given
        authService.save(유저_회원가입_DTO);
        UserLoginReqDto 예외발생요청 = new UserLoginReqDto(일반_사용자_이메일, "다른패스워드");

        //given & when & then
        assertThatThrownBy(() -> authService.login(예외발생요청))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("패스워드가 일치하지 않습니다.");
    }
}
