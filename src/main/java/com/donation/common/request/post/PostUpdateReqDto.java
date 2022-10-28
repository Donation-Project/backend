package com.donation.common.request.post;

import com.donation.domain.enums.Category;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostUpdateReqDto {
    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    @NotNull(message = "후원량이 없습니다.")
    private String amount;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    @Builder
    public PostUpdateReqDto(String title, String content, String amount, Category category) {
        this.title = title;
        this.content = content;
        this.amount = amount;
        this.category = category;
    }
}
