package com.donation.common.response.donation;

import com.donation.domain.entites.Donation;
import com.donation.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DonationFindRespDto {

    private String title;
    private float amount;
    private float total;
    private Long postId;
    private Category category;
    private LocalDateTime localDateTime;


    @Builder
    @QueryProjection
    public DonationFindRespDto(String title, float amount, float total,Long postId, Category category, LocalDateTime localDateTime) {
        this.title = title;
        this.amount = amount;
        this.total = total;
        this.postId=postId;
        this.category = category;
        this.localDateTime = localDateTime;
    }
}
