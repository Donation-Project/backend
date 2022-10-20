package com.donation.common.request.donation;

import com.donation.domain.enums.Category;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DonationFilterDto {
    String username;
    Category category;


    @Builder
    public DonationFilterDto(String username, Category category) {
        this.username = username;
        this.category = category;
    }
}
