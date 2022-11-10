package com.donation.domain.comment.entity;

import com.donation.global.exception.DonationInvalidateException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class Message {

    private static final int MAX_MESSAGE_LENGTH = 100;

    @Column(name = "message", nullable = false)
    private String value;

    @Builder
    public Message(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value){
        if (value == null || value.isBlank()) {
            throw new DonationInvalidateException("댓글의 내용이 존재하지 않습니다.");
        }
        if (value.length() > MAX_MESSAGE_LENGTH){
            throw new DonationInvalidateException("댓글의 길이가 제한을 초과합니다.");
        }
    }
}
