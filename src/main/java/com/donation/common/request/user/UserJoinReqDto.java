package com.donation.common.request.user;

import com.donation.domain.entites.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserJoinReqDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    public User toUser(){
        return User.builder()
                .username(this.email)
                .name(this.name)
                .password(this.password)
                .build();
    }

    @Builder
    public UserJoinReqDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
}
