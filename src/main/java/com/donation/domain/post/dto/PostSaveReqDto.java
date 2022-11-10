package com.donation.domain.post.dto;

import com.donation.infrastructure.embed.Write;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.post.entity.Category;
import com.donation.domain.post.entity.PostState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PostSaveReqDto {

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    @NotNull(message = "후원량이 없습니다.")
    private String amount;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    String image;

    @Builder
    public PostSaveReqDto(String title, String content, String amount, Category category, String image) {
        this.title = title;
        this.content = content;
        this.amount = amount;
        this.category = category;
        this.image = image;
    }

    public Post toPost(User user) {
        return Post.builder()
                .user(user)
                .write(new Write(title, content))
                .amount(amount)
                .category(category)
                .state(PostState.APPROVAL)
                .build();
    }
}
