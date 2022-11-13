package com.donation.domain.reviews.dto;

import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.infrastructure.embed.Write;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRespDto {

    private Long reviewId;
    private Write write;
    private UserRespDto userRespDto;

    @Builder
    public ReviewRespDto(Long reviewId, Write write, UserRespDto userRespDto) {
        this.reviewId = reviewId;
        this.write = write;
        this.userRespDto = userRespDto;
    }

    public static ReviewRespDto of(Reviews review){
        return ReviewRespDto.builder()
                .reviewId(review.getId())
                .write(review.getWrite())
                .userRespDto(UserRespDto.of(review.getUser()))
                .build();

    }
}
