package com.donation.common.response.post;

import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

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
    public PostRespDto(Long id, String username, Write write, Integer amount, Category category, PostState state, LocalDateTime createAt) {
        this.id = id;
        this.username = username;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.createAt = createAt;
    }
}
