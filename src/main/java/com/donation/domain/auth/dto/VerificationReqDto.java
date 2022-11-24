package com.donation.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class VerificationReqDto {

    @NotEmpty
    private String email;

    @NotEmpty
    public String code;

    @Builder
    public VerificationReqDto(final String email, final String code) {
        this.email = email;
        this.code = code;
    }
}
