package com.donation.common.request.post;

import com.donation.domain.enums.Category;
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
    private Integer amount;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;


}
