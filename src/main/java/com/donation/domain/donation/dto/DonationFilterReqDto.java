package com.donation.domain.donation.dto;

import com.donation.domain.post.entity.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class DonationFilterReqDto {

    @Nullable
    String username;

    @Nullable
    Category category ;


    @Builder
    public DonationFilterReqDto(String username, Category category) {
        this.username = username;
        this.category = category;
    }

}
