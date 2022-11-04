package com.donation.auth;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginMember {
    private Long id;

    public LoginMember(final Long id) {
        this.id = id;
    }

}