package com.donation.domain.auth.application;

import com.donation.domain.auth.dto.VerificationReqDto;
import com.donation.domain.auth.entity.AuthCode;
import com.donation.domain.auth.repository.AuthCodeRepository;
import com.donation.infrastructure.support.AuthEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthCodeService {

    private final AuthEncoder authEncoder;
    private final AuthCodeRepository authCodeRepository;

    @Transactional
    public void createAuthCodeAndSave(String authSerialNumber, String authCodeText){
        authCodeRepository.deleteByAuthSerialNumber(authSerialNumber);
        AuthCode authCode = AuthCode.of(authCodeText, authSerialNumber);
        authCodeRepository.save(authCode);
    }

    public void verifyCode(VerificationReqDto verificationReqDto){
        String authSerialNumber = authEncoder.encodeNoSalt(verificationReqDto.getEmail());
        AuthCode authCode = authCodeRepository.getByAuthSerialNumber(authSerialNumber);
        authCode.verify(verificationReqDto.getCode());
        authCode.verifyTime(LocalDateTime.now());
    }
}
