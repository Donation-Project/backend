package com.donation.common.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordModifyReqDto {

    private String currentPassword;
    private String modifyPassword;

    public UserPasswordModifyReqDto(final String currentPassword,final String modifyPassword) {
        this.currentPassword = currentPassword;
        this.modifyPassword = modifyPassword;
    }
}
