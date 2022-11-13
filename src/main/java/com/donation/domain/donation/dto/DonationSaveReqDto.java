package com.donation.domain.donation.dto;

import com.donation.presentation.auth.LoginMember;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DonationSaveReqDto {

    @NotNull(message = "유저의 아이디가 없습니다")
    private Long userId;

    @NotNull(message = "포스트의 아이디가 없습니다")
    private Long postId;

    @NotNull(message = "후원량이 없습니다.")
    @Max(value = 10000)
    @Min(value = 0)
    private String amount;

    @Builder
    public DonationSaveReqDto(Long userId, Long postId, String amount) {
        this.userId = userId;
        this.postId = postId;
        this.amount = amount;
    }

    public float getFloatAmount(){
        return Float.parseFloat(this.amount);
    }

}
