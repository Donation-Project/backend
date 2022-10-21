package com.donation.common.request.donation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DonationSaveReqDto {

    @NotNull(message = "후원자의 아이디가 없습니다")
    private Long userId;

    @NotNull(message = "포스트의 아이디가 없습니다")
    private Long postId;

    @NotNull(message = "후원량이 없습니다.")
    @Max(value = 10000)
    @Min(value = 0)
    private float amount;

    @Builder
    public DonationSaveReqDto(Long userId, Long postId, float amount) {
        this.userId = userId;
        this.postId = postId;
        this.amount = amount;
    }


}
