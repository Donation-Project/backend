package com.donation.common.request.user;

import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
@Slf4j
public class UserJoinReqDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;


    public User toUser(String imageUrl){
        return User.builder()
                .username(this.email)
                .name(this.name)
                .password(this.password)
                .profileImage(imageUrl)
                .role(Role.USER)
                .build();
    }

    @Builder
    public UserJoinReqDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }


}
