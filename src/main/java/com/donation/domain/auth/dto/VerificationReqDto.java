package com.donation.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerificationReqDto {
    private String email;
    public String code;

    @Builder
    public VerificationReqDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
