package com.donation.repository.auth;

import com.donation.auth.repository.TokenRepository;
import com.donation.exception.DonationNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
public class InMemoryTokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @AfterEach
    void setUp(){
        tokenRepository.deleteAll();
    }

    @Test
    @DisplayName("토큰을 저장한다.")
    void 토큰을_저장한다(){
        //given
        Long tempMemberId = 1L;
        String tempRefreshToken = "token";

        //when
        tokenRepository.save(tempMemberId, tempRefreshToken);

        //then
        assertThat(tokenRepository.getToken(tempMemberId)).isEqualTo(tempRefreshToken);
    }

    @Test
    @DisplayName("Id에 해당하는 토큰이 있으면 true 반환")
    void Id에_해당하는_토큰이_있으면_true를_반환(){
        //given
        Long tempMemberId = 1L;
        String tempRefreshToken = "token";
        tokenRepository.save(tempMemberId, tempRefreshToken);

        //when & then
        assertThat(tokenRepository.exist(tempMemberId)).isTrue();
    }

    @Test
    @DisplayName("Id에 해당하는 토큰을 가져온다.")
    void Id에_해당하는_토큰을_가져온다(){
        //given
        Long tempMemberId = 1L;
        String tempRefreshToken = "token";
        tokenRepository.save(tempMemberId, tempRefreshToken);

        //when
        String actual = tokenRepository.getToken(tempMemberId);

        //then
        assertThat(actual).isEqualTo(tempRefreshToken);
    }

    @Test
    @DisplayName("Id에 해당하는 토큰이 없으면 예외발생")
    void Id에_해당하는_토큰이_없으면_예외발생(){
        //given
        Long tempMemberId = 0L;

        //when & then
        assertThatThrownBy(() -> tokenRepository.getToken(tempMemberId))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("일치하는 토큰이 존재하지 않습니다.");
    }
}
