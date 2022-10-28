package com.donation.common.request.user;

import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserSaveReqDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @NotBlank
    private String metamask;


    public User toUser(String imageUrl){
        return User.builder()
                .email(this.email)
                .name(this.name)
                .password(this.password)
                .profileImage(imageUrl)
                .role(Role.USER)
                .metamask(this.metamask)
                .build();
    }

    @Builder
    public UserSaveReqDto(String email, String name, String password, String metamask) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.metamask = metamask;
    }
}
