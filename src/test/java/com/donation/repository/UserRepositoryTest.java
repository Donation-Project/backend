package com.donation.repository;

import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    static User getUser() {
        String username = "username@naver.com";
        String name = "정우진";
        String password = "1234";
        Role role = Role.USER;

        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .role(role)
                .build();
    }


    @Test
    @DisplayName("회원 : 로그인(이메일, 패스워드) 조회")
    void login(){
        //given
        User user = getUser();
        userRepository.save(user);

        String email = user.getUsername();
        String password = user.getPassword();

        //when
        User findUser = userRepository.findByUsernameAndPassword(email, password).get();

        //then
        assertThat(findUser.getId()).isEqualTo(user.getId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("회원 : 로그인(이메일, 패스워드) 검색결과 없음")
    void login_none(){
        //given
        String email = "none@naver.com";
        String password = "nonPassword";

        //when
        User findUser = userRepository.findByUsernameAndPassword(email, password)
                .orElse(null);

        //then
        assertThat(findUser).isNull();
    }
}