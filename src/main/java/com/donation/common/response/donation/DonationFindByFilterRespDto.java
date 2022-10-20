package com.donation.common.response.donation;

import com.donation.domain.enums.Category;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonationFindByFilterRespDto {

    private Long donateId;
    private String title;
    private float amount;
    private String sponsor;
    private String beneficiary;
    private LocalDateTime localDateTime;
    private float total;
    private Category category;

    @Builder
    @QueryProjection
    public DonationFindByFilterRespDto(Long donateId, String title, float amount, String sponsor, String beneficiary, LocalDateTime localDateTime, float total, Category category) {
        this.donateId = donateId;
        this.title = title;
        this.amount = amount;
        this.sponsor = sponsor;
        this.beneficiary = beneficiary;
        this.localDateTime = localDateTime;
        this.total = total;
        this.category = category;
    }
}
