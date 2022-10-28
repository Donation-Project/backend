package com.donation.common.response.donation;

import com.donation.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonationFindRespDto {

    private String title;
    private String amount;
    private String total;
    private Long postId;
    private Category category;
    private LocalDateTime localDateTime;


    @Builder
    @QueryProjection
    public DonationFindRespDto(String title, String amount, Long postId, Category category, LocalDateTime localDateTime) {
        this.title = title;
        this.amount = amount;

        this.postId=postId;
        this.category = category;
        this.localDateTime = localDateTime;
    }
    public void setTotal(float total){
        this.total =String.valueOf(total);
    }

}
