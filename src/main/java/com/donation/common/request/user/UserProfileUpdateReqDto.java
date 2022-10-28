package com.donation.common.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateReqDto {
    @NotBlank
    private String profileImage;

    public UserProfileUpdateReqDto(final String profileImage) {
        this.profileImage = profileImage;
    }
}
