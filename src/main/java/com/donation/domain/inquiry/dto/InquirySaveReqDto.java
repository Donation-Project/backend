package com.donation.domain.inquiry.dto;

import com.donation.domain.inquiry.entity.InquiryState;
import com.donation.domain.post.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
public class InquirySaveReqDto {

    private Long Inquiry_id;

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    private Long user_id;

    private InquiryState inquiryState;

    @Builder
    public InquirySaveReqDto(Long inquiry_id,String title, String content, Long user_id, InquiryState inquiryState) {
        this.Inquiry_id = inquiry_id;
        this.title = title;
        this.content = content;
        this.user_id = user_id;
        this.inquiryState = inquiryState;
    }
}
