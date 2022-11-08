package com.donation.common.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UserPasswordModifyReqDto {

    @NotBlank
    private String currentPassword;
    @NotBlank
    private String modifyPassword;

    public UserPasswordModifyReqDto(final String currentPassword,final String modifyPassword) {
        this.currentPassword = currentPassword;
        this.modifyPassword = modifyPassword;
    }
}
