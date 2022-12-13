package com.donation.domain.inquiry.dto;

import com.donation.domain.inquiry.entity.InquiryState;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class InquiryUpdateReqDto {

    private Long Inquiry_id;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    @Builder
    public InquiryUpdateReqDto(Long inquiry_id,String title, String content) {
        this.Inquiry_id = inquiry_id;
        this.title = title;
        this.content = content;
    }
}
