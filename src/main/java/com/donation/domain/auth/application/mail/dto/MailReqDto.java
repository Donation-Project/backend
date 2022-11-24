package com.donation.domain.auth.application.mail.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class MailReqDto {

    @NotEmpty
    private String email;

    @Builder
    public MailReqDto(String email) {
        this.email = email;
    }
}
