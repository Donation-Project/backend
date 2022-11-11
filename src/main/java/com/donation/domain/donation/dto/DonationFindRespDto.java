package com.donation.domain.donation.dto;

import com.donation.domain.donation.entity.Donation;
import com.donation.domain.post.entity.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DonationFindRespDto {

    private String title;
    private String amount;

    private float grossAmount;
    private Long postId;

    private Long userId;
    private Category category;


    public static DonationFindRespDto of(Donation donation,float grossAmount){
        return DonationFindRespDto.builder()
                .title(donation.getPost().getWrite().getTitle())
                .amount(donation.getAmount())
                .grossAmount(grossAmount)
                .postId(donation.getPost().getId())
                .userId(donation.getUser().getId())
                .category(donation.getPost().getCategory())
                .build();
    }


    @Builder
    @QueryProjection
    public DonationFindRespDto(String title, String amount, float grossAmount, Long postId, Long userId, Category category) {
        this.title = title;
        this.amount = amount;
        this.grossAmount = grossAmount;
        this.postId = postId;
        this.userId = userId;
        this.category = category;

    }
}
