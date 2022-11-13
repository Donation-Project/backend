package com.donation.infrastructure.embed;

import com.donation.global.exception.DonationInvalidateException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Getter
@NoArgsConstructor
public class Write {


    private static final int TITLE_MESSAGE_LENGTH = 15;
    private String title;
    @Lob
    private String content;

    @Builder
    public Write(String title, String content) {
        validate(title, content);
        this.title = title;
        this.content = content;
    }

    public static Write of(String title, String content){
        return Write.builder()
                .title(title)
                .content(content)
                .build();
    }

    private void validate(final String title, final String content){
        if (title == null || content == null)
            throw new DonationInvalidateException("제목 또는 내용이 존재하지 않습니다.");
        if (title.length() > TITLE_MESSAGE_LENGTH)
            throw new DonationInvalidateException("제목이 15자 이상일 수 없습니다.");

    }
}