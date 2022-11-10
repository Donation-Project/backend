package com.donation.domain.user.repository;

import com.donation.domain.user.dto.UserRespDto;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.global.exception.DonationDuplicateException;
import com.donation.global.exception.DonationNotFoundException;
import com.donation.infrastructure.support.PageCustom;
import com.donation.common.utils.RepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static com.donation.common.UserFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("존재하지 않는 회원번호로 조회시 예외를 던진다.")
    void 존재하지_않는_회원번호로_조회시_예외를_던진다(){
        //given
        Long id = 0L;

        //when & then
        assertThatThrownBy(() -> userRepository.getById(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("회원번호 검증시 회원이 존재하면 예외가 발생하지 않는다.")
    void 회원번호_검증시_회원이_존재하면_예외가_발생하지_않는다(){
        //given
        Long id = userRepository.save(createUser()).getId();

        Assertions.assertDoesNotThrow(() -> userRepository.validateExistsById(id));
    }

    @Test
    @DisplayName("존재하지 않는 회원번호로 검증시 예외를 던진다")
    void 존재하지_않는_회원번호로_검증시_예외를_던진다(){
        //given
        Long id = 0L;

        //when & then
        assertThatThrownBy(() -> userRepository.validateExistsById(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 회원번호입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원 검증시 예외를 던진다.")
    void 존재하지_않는_이메일로_회원_검증시_예외를_던진다(){
        //given
        String email = "dummyEmail@email.com";

        //when & then
        assertThatThrownBy(() -> userRepository.getByEmail(email))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    @DisplayName("중복된 이메일로 회원검증시 예외를 던진다.")
    void 중복된_이메일로_회원검증시_예외를_던진다(){
        //given
        User 일반 = userRepository.save(createUser());

        //when & then
        assertThatThrownBy(() -> userRepository.validateExistsByEmail(일반.getEmail()))
                .isInstanceOf(DonationDuplicateException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("회원 페이징으로 조회")
    void 회원을_페이징으로_조회(){
        //given
        userRepository.saveAll(creatUserList(0, 20));

        //when
        PageCustom<UserRespDto> actual = userRepository.getPageDtoList(PageRequest.of(0, 10));

        //then
        Assertions.assertAll(() -> {
            assertThat(actual.getPage().getTotalElement()).isEqualTo(20);
            assertThat(actual.getPage().getTotalPages()).isEqualTo(2);
            assertThat(actual.getContent().size()).isEqualTo(10);
            assertThat(actual.getPage().getPageNum()).isEqualTo(1);
        });
    }
}