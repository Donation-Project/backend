package com.donation.common.request.donation;

import com.donation.domain.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
