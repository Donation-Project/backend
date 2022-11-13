package com.donation.domain.donation.dto;

import com.donation.domain.post.entity.Category;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@EqualsAndHashCode
public class DonationFilterReqDto {

    @Nullable
    String name;

    @Nullable
    Category category ;


    @Builder
    public DonationFilterReqDto(String name, Category category) {
        this.name = name;
        this.category = category;
    }

}
