package com.donation.domain.post.dto;

import com.donation.infrastructure.embed.Write;
import com.donation.domain.post.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
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

    public Write getWrite(){
        return Write.builder()
                .title(title)
                .content(content)
                .build();
    }
}
