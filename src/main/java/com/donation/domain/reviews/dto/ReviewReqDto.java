package com.donation.domain.reviews.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ReviewReqDto {

    private String title;
    private String content;

    @Builder
    public ReviewReqDto(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
