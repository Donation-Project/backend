package com.donation.domain.user.application;

import com.donation.domain.user.service.UserService;
import com.donation.presentation.auth.LoginMember;
import com.donation.domain.user.dto.UserLoginReqDto;
import com.donation.domain.user.dto.UserSaveReqDto;
import com.donation.domain.user.dto.UserEmailRespDto;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.user.entity.User;
import com.donation.global.exception.DonationInvalidateException;
import com.donation.domain.user.repository.UserRepository;
import com.donation.infrastructure.util.PageCustom;
import com.donation.domain.auth.application.AuthService;
import com.donation.infrastructure.Image.AwsS3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


class UserServiceTest extends ServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("20명의 회원을 10개씩 페이징후 조회")
    void 회원20명의_정보를_10명씩_페이징_조회() {
        //given
        List<User> users = creatUserList(0, 20);
        userRepository.saveAll(users);

        //when
        PageCustom<UserRespDto> actual = userService.getList(PageRequest.of(0, 10));

        //then
        assertAll(() -> {
            assertThat(actual.getPage().getTotalElement()).isEqualTo(20);
            assertThat(actual.getPage().getTotalPages()).isEqualTo(2);
            assertThat(actual.getContent().size()).isEqualTo(10);
            assertThat(actual.getPage().getPageNum()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("저장된 회원 새로운 프로필 등록")
    void 저장된_회원_새로운_프로필_등록() {
        //given
        User user = userRepository.save(createUser());
        LoginMember loginMember = new LoginMember(user.getId());
        //when
        userService.updateProfile(loginMember, 유저_프로필_업데이트_DTO);

        //then
        User actual = userRepository.getById(user.getId());
        assertThat(actual.getProfileImage()).isNotEqualTo(일반_사용자_프로필);

        //clear
        awsS3Service.delete(actual.getProfileImage());
    }

    @Test
    @DisplayName("회원 id를 통해 회원 조회")
    void 회원_ID를_통해_회원_조회() {
        //given
        User user = userRepository.save(createUser());
        LoginMember loginMember = new LoginMember(user.getId());

        //when
        UserRespDto dto = userService.findById(loginMember);

        //then
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("이메일이 존재한다면 (이미 중복된 이메일이 존재합니다.)메시지를 전달한다")
    void 이메일이_존재한다면_이미_중복된_이메일이_존재합니다_메시지를_전달한다() {
        //given
        User user = userRepository.save(createUser());

        //when
        UserEmailRespDto userEmailRespDto = userService.checkUniqueEmail(user.getEmail());

        //then
        assertThat(userEmailRespDto.getMessage()).isEqualTo("이미 중복된 이메일이 존재합니다.");
    }


    @ParameterizedTest
    @DisplayName("이메일이 중복되지않는다면 (사용가능한 이메일입니다.)메시지를 전달한다")
    @ValueSource(strings = {"default@email.com"})
    void 이메일이_중복되지않는다면_사용가능한_이메일입니다_메시지를_전달한다(final String email) {
        //given & when
        UserEmailRespDto userEmailRespDto = userService.checkUniqueEmail(email);

        //then
        assertThat(userEmailRespDto.getMessage()).isEqualTo("사용가능한 이메일입니다.");
    }

    @ParameterizedTest
    @DisplayName("사용자의 비밀번호를 변경합니다.")
    @ValueSource(strings = {"password"})
    void 사용자의_비밀번호를_변경합니다(final String password) {
        //given
        UserSaveReqDto user = UserSaveReqDto.builder()
                .email(일반_사용자_이메일)
                .password(password)
                .metamask(일반_사용자_메타마스크_주소)
                .name(일반_사용자_이름)
                .build();
        Long id = authService.save(user);

        //when
        userService.passwordModify(회원검증(id), 비밀번호_변경_DTO(password, 새로운_일반_사용자_패스워드));

        //then
        assertDoesNotThrow(() -> authService.login(new UserLoginReqDto(일반_사용자_이메일, 새로운_일반_사용자_패스워드)));
    }

    @ParameterizedTest
    @DisplayName("사용자의 비밀번호를 변경할때 현재비밀번호가 일치하지않으면 예외를 던진다.")
    @ValueSource(strings = {"잘못된 현재 비밀번호"})
    void 사용자의_비밀번호를_변경할때_현재_비밀번호가_일치하지않으면_예외를_던진다(final String 잘못된비밀번호) {
        //given
        Long id = authService.save(유저_회원가입_DTO);

        //when & then
        assertThatThrownBy(() -> userService.passwordModify(회원검증(id), 비밀번호_변경_DTO(잘못된비밀번호, 새로운_일반_사용자_패스워드)))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("인증정보가 일치하지 않습니다.");
    }
}