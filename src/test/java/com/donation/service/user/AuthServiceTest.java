package com.donation.service.user;

import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.utils.ServiceTest;
import com.donation.exception.DonationDuplicateException;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.user.UserRepository;
import com.donation.service.user.auth.application.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AuthServiceTest extends ServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

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
        authService.save(유저_회원가입_DTO);

        //when & then
        assertDoesNotThrow(() -> authService.login(유저_로그인_DTO));
    }

    @Test
    @DisplayName("다른 패스워드로 로그인 요청시 예외 발생")
    void 다른_패스워드로_로그인_요청시_예외_발생(){
        //given
        authService.save(유저_회원가입_DTO);
        UserLoginReqDto 예외발생요청 = new UserLoginReqDto(일반_사용자_이메일, "다른패스워드", null);

        //given & when & then
        assertThatThrownBy(() -> authService.login(예외발생요청))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("패스워드가 일치하지 않습니다.");
    }
}
