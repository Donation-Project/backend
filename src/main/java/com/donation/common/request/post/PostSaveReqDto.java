package com.donation.common.request.post;

import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PostSaveReqDto {
    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    @NotNull(message = "후원량이 없습니다.")
    private Integer amount;

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;

    public Post toPost(User user) {
        return Post.builder()
                .user(user)
                .write(toWrite())
                .amount(amount)
                .category(category)
                .state(PostState.WAITING)
                .build();
    }

    public Write toWrite(){
        return Write.builder()
                .title(title)
                .content(content)
                .build();
    }
}
