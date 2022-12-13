package com.donation.domain.inquiry.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class InquiryFindReqDto {
    private Long Inquiry_id;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    private Long user_id;

    @Builder
    public InquiryFindReqDto(Long inquiry_id,String title, String content,Long user_id) {
        this.Inquiry_id = inquiry_id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
    }
}
