package com.donation.auth;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class LoginMember {
    private Long id;

    public LoginMember(final Long id) {
        this.id = id;
    }

}