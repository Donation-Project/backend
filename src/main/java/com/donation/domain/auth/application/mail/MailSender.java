package com.donation.domain.auth.application.mail;

public interface MailSender {
    void sender(String email, String authCode);
}
