package com.donation.common.response.donation;

import com.donation.domain.entites.Donation;
import com.donation.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonationFindRespDto {

    private String title;
    private String amount;

    private float currentAmount;
    private Long postId;

    private Long userId;
    private Category category;


    public static DonationFindRespDto of(Donation donation){
        return DonationFindRespDto.builder()
                .title(donation.getPost().getWrite().getTitle())
                .amount(donation.getAmount())
                .currentAmount(donation.getPost().getCurrentAmount())
                .postId(donation.getPost().getId())
                .userId(donation.getUser().getId())
                .category(donation.getPost().getCategory())
                .build();
    }


    @Builder
    @QueryProjection
    public DonationFindRespDto(String title, String amount, float currentAmount, Long postId, Long userId, Category category) {
        this.title = title;
        this.amount = amount;
        this.currentAmount = currentAmount;
        this.postId = postId;
        this.userId = userId;
        this.category = category;

    }
}
