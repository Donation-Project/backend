package com.donation.common.request;

import com.donation.domain.entites.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class UserJoinReqDto {
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
}
