package com.donation.service.user;

import com.donation.common.utils.ServiceTest;
import com.donation.exception.DonationDuplicateException;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.UserFixtures.유저_회원가입_DTO;
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
}
