package com.donation.domain.user.dto;

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
