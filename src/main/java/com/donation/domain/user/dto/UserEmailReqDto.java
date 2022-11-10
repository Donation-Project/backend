package com.donation.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserEmailReqDto {

    @Email
    @NotBlank
    private String email;

    public UserEmailReqDto(String email) {
        this.email = email;
    }
}
