package com.donation.domain.auth.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class AuthCode extends BaseEntity {

    private static final Long EXPIRED_TIME = 5L;

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(unique = true, nullable = false)
    private String authSerialNumber;

    @Builder
    public AuthCode(final String code,final String authSerialNumber) {
        this.code = code;
        this.authSerialNumber = authSerialNumber;
    }

    public static AuthCode of(final String code, final String authSerialNumber){
        return AuthCode.builder()
                .code(code)
                .authSerialNumber(authSerialNumber)
                .build();
    }

    public void verify(String code){
        if (!this.code.equals(code))
            throw new DonationInvalidateException("인증 코드가 일치하지 않습니다.");
    }

    public void verifyTime(LocalDateTime currentTime){
        LocalDateTime expireTime = getUpdateAt().plusMonths(EXPIRED_TIME);
        if(currentTime.isAfter(expireTime))
            throw new DonationInvalidateException("인증 시간이 5분을 초과했습니다");
    }
}
