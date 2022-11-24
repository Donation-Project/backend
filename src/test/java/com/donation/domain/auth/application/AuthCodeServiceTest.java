package com.donation.domain.auth.application;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.auth.entity.AuthCode;
import com.donation.domain.auth.repository.AuthCodeRepository;
import com.donation.infrastructure.support.AuthEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.AuthFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class AuthCodeServiceTest extends ServiceTest {

    @Autowired
    private AuthEncoder authEncoder;
    @Autowired
    private AuthCodeService authCodeService;
    @Autowired
    private AuthCodeRepository authCodeRepository;

    @Test
    @DisplayName("인증코드가 정상적으로 생성된다.")
    public void 인증코드가_정상적으로_생성된다(){
        //given & when & then
        Assertions.assertDoesNotThrow(() -> authCodeService.createAuthCodeAndSave(시리얼넘버, 인증코드));
    }

    @Test
    @DisplayName("인증코드 저장시 기존에 저장된 인증코드를 삭제하고 저장된다.")
    public void 인증코드_저장시_기존에_저장된_인증코드를_삭제하고_저장된다(){
        //given
        AuthCode expected = authCodeRepository.save(인증코드());

        //when
        authCodeService.createAuthCodeAndSave(시리얼넘버, 인증코드);
        AuthCode actual = authCodeRepository.getByAuthSerialNumber(시리얼넘버);

        //then
        assertThat(actual.getCreateAt().isAfter(expected.getCreateAt())).isTrue();
        assertThat(actual.getId()).isNotEqualTo(expected.getId());
    }

    @Test
    @DisplayName("인증코드가 정상적으로 검증된다.")
    public void 인증코드가_정상적으로_검증된다(){
        //given
        AuthCode expected = authCodeRepository.save(AuthCode.of(인증코드, authEncoder.encodeNoSalt(시리얼넘버)));

        //when
        Assertions.assertDoesNotThrow(() -> authCodeService.verifyCode(검증_DTO()));
    }
}