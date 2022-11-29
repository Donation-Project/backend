package com.donation.domain.auth.application.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender{

    private static final String MAIL_SUBJECT_TEXT = "회원 가입 인증코드입니다.";

    private final MailProperties properties;
    private final JavaMailSender javaMailSender;

    @Override
    public void sender(String email, String authCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(properties.getUsername());
        message.setTo(email);
        message.setSubject(MAIL_SUBJECT_TEXT);
        message.setText(authCode);

        javaMailSender.send(message);
    }
}
