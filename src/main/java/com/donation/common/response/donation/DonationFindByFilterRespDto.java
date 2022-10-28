package com.donation.common.response.donation;

import com.donation.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonationFindByFilterRespDto {

    private Long donateId;
    private Long postId;
    private String title;
    private String amount;
    private String sponsor;
    private String beneficiary;
    private LocalDateTime localDateTime;
    private String total;
    private Category category;

    @Builder
    @QueryProjection
    public DonationFindByFilterRespDto(Long donateId, Long postId, String title, String amount, String sponsor, String beneficiary, LocalDateTime localDateTime, Category category) {
        this.donateId = donateId;
        this.postId = postId;
        this.title = title;
        this.amount = amount;
        this.sponsor = sponsor;
        this.beneficiary = beneficiary;
        this.localDateTime = localDateTime;
        this.category = category;
    }

    public void setTotal(float total){
        this.total =String.valueOf(total);
    }
}
