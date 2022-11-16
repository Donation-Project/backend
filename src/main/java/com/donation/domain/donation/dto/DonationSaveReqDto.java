package com.donation.domain.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DonationSaveReqDto {

    @NotNull(message = "후원량이 없습니다.")
    @Max(value = 10000)
    @Min(value = 0)
    private String amount;

    @Builder
    public DonationSaveReqDto( String amount) {
        this.amount = amount;
    }

    public float getFloatAmount(){
        return Float.parseFloat(this.amount);
    }

}
