package com.donation.domain.reviews.dto;

import com.donation.infrastructure.embed.Write;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewReqDto {

    private Write write;

    @Builder
    public ReviewReqDto(Write write) {
        this.write = write;
    }
}
