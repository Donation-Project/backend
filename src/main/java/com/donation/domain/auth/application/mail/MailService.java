package com.donation.domain.auth.application.mail;

import com.donation.domain.auth.application.AuthCodeService;
import com.donation.domain.auth.application.mail.dto.MailReqDto;
import com.donation.infrastructure.support.AuthCodeGenerator;
import com.donation.infrastructure.support.AuthEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final AuthCodeGenerator authCodeGenerator;
    private final AuthEncoder authEncoder;
    private final MailSender mailSender;
    private final AuthCodeService authCodeService;

    public void sendCodeToMailAndAuthCodeSave(MailReqDto mailReqDto){
        String serialNumber = authEncoder.encodeNoSalt(mailReqDto.getEmail());
        String authCodeText = authCodeGenerator.codeGenerator();
        authCodeService.createAuthCodeAndSave(serialNumber, authCodeText);
        sendCodeToMail(mailReqDto.getEmail(), authCodeText);
    }

    private void sendCodeToMail(String email, String authCodeText){
        mailSender.sender(email, authCodeText);
    }
}
