package com.donation.common.request.post;

import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PostSaveReqDto {
    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    @NotNull(message = "후원량이 없습니다.")
    private float amount;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    @Builder
    public PostSaveReqDto(String title, String content, float amount, Category category) {
        this.title = title;
        this.content = content;
        this.amount = amount;
        this.category = category;
    }

    public Post toPost(User user) {
        return Post.builder()
                .user(user)
                .write(new Write(title, content))
                .amount(amount)
                .category(category)
                .state(PostState.WAITING)
                .build();
    }
}
