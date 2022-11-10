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

    private float gross_amount;
    private Long postId;

    private Long userId;
    private Category category;


    public static DonationFindRespDto of(Donation donation,float gross_amount){
        return DonationFindRespDto.builder()
                .title(donation.getPost().getWrite().getTitle())
                .amount(donation.getAmount())
                .gross_amount(gross_amount)
                .postId(donation.getPost().getId())
                .userId(donation.getUser().getId())
                .category(donation.getPost().getCategory())
                .build();
    }


    @Builder
    @QueryProjection
    public DonationFindRespDto(String title, String amount, float gross_amount, Long postId, Long userId, Category category) {
        this.title = title;
        this.amount = amount;
        this.gross_amount = gross_amount;
        this.postId = postId;
        this.userId = userId;
        this.category = category;

    }
}
