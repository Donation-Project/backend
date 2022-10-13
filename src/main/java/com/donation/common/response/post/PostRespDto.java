package com.donation.common.response.post;

import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
public class PostRespDto {

    private Long id;
    private String username;

    private Write write;
    private Integer amount;
    private Category category;
    private PostState state;
    private LocalDateTime createAt;

    @QueryProjection
    public PostRespDto(Long userid,String username,  Write write, Integer amount, Category category, PostState state, LocalDateTime createAt) {
        this.id = userid;
        this.username = username;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.createAt = createAt;
    }

    public static PostRespDto toDto(Post post) {
        return new PostRespDto(
                post.getUser().getId(),
                post.getUser().getName(),
                post.getWrite(),
                post.getAmount(),
                post.getCategory(),
                post.getState(),
                post.getUser().getCreateAt());
    }



}
