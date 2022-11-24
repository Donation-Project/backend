package com.donation.domain.auth.repository;

import com.donation.common.utils.RepositoryTest;
import com.donation.domain.auth.entity.AuthCode;
import com.donation.global.exception.DonationInvalidateException;
import com.donation.global.exception.DonationNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static com.donation.common.AuthFixtures.시리얼넘버;
import static com.donation.common.AuthFixtures.인증코드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthCodeRepositoryTest extends RepositoryTest {

    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Test
    @DisplayName("인증코드 생성 후 5분 이후 인증시 오류를 던진다.")
    void 인증코드_생성_후_5분_이후_인증시_오류를_던진다(){
        //given
        LocalDateTime verificationTime = LocalDateTime.now().plusMinutes(10);
        AuthCode actual = authCodeRepository.save(인증코드());

        //when & then
        assertThatThrownBy(() -> actual.verifyTime(verificationTime))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("인증 시간이 5분을 초과했습니다.");
    }

    @Test
    @DisplayName("시리얼넘버를 통해 정상적으로 조회된다.")
    void 시리얼넘버를_통해_정상적으로_조회된다(){
        //given
        authCodeRepository.save(인증코드());
        String expected = 시리얼넘버;

        //when
        AuthCode actual = authCodeRepository.getByAuthSerialNumber(expected);

        //then
        assertThat(actual.getAuthSerialNumber()).isEqualTo(expected);
    }

    @Test
    @DisplayName("시리얼넘버가 유효하지 않은 경우 조회시 오류를 던진다.")
    void 시리얼넘버가_유효하지_않은_경우_조회시_오류를_던진다(){
        //given
        String expected = 시리얼넘버;

        //when & then
        assertThatThrownBy(() -> authCodeRepository.getByAuthSerialNumber(expected))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("인증 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("시리얼 넘버가 존재할 경우 정상적으로 로직을 수행한다.")
    void 시리얼넘버가_존재할_경우_정상적으로_로직을_수행한다(){
        //given
        authCodeRepository.save(인증코드());
        String expected = 시리얼넘버;

        //when & then
        Assertions.assertDoesNotThrow(() -> authCodeRepository.validateExistByAuthSerialNumber(expected));
    }

    @Test
    @DisplayName("시리얼 넘버가 존재하지 않을 경우 오류를 던진다.")
    void 시리얼_넘버가_존재하지_않을_경우_오류를_던진다(){
        //given
        String expected = 시리얼넘버;

        //when & then
        assertThatThrownBy(() -> authCodeRepository.validateExistByAuthSerialNumber(expected))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("발급된 인증코드가 존재하지 않습니다.");
    }
}