package com.donation.service.user;

import com.donation.common.response.user.UserRespDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.User;
import com.donation.exception.DonationException;
import com.donation.repository.user.UserRepository;
import com.donation.repository.utils.PageCustom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class UserServiceTest extends ServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("20명의 회원을 10개씩 페이징후 조회")
    void 회원20명의_정보를_10명씩_페이징_조회(){
        //given
        List<User> users = creatUserList(0, 20);
        userRepository.saveAll(users);

        //when
        PageCustom<UserRespDto> actual = userService.getList(PageRequest.of(0, 10));

        //then
        Assertions.assertAll(() -> {
            assertThat(actual.getPage().getTotalElement()).isEqualTo(20);
            assertThat(actual.getPage().getTotalPages()).isEqualTo(2);
            assertThat(actual.getContent().size()).isEqualTo(10);
            assertThat(actual.getPage().getPageNum()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("저장된 회원 새로운 프로필 등록")
    void 저장된_회원_새로운_프로필_등록(){
        //given
        User user = userRepository.save(createUser());

        //when
        userService.updateProfile(user.getId(), 유저_프로필_업데이트_DTO);

        //then
        User actual = userRepository.getById(user.getId());
        assertThat(actual.getProfileImage()).isNotEqualTo(일반_사용자_프로필);

        //clear
        userService.delete(actual.getId());
    }

    @Test
    @DisplayName("회원 id를 통해 회원 조회")
    void 회원_ID를_통해_회원_조회() {
        //given
        User user = userRepository.save(createUser());

        //when
        UserRespDto dto = userService.findById(user.getId());

        //then
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getName()).isEqualTo(user.getName());
    }


    @Test
    @DisplayName("회원 ID를 통해 회원 삭제")
    void 회원_ID를_통해_회원_삭제(){
        //given
        User user = userRepository.save(createUser());

        //when
        userService.delete(user.getId());

        //then
        assertThatThrownBy(() -> userService.findById(user.getId()))
                .isInstanceOf(DonationException.class);
    }
}