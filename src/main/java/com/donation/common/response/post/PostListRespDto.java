package com.donation.common.response.post;

import com.donation.common.response.user.UserRespDto;
import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostListRespDto {

    private Long postId;

    private UserRespDto userRespDto;

    private Write write;

    private String amount;

    private Category category;

    private PostState state;

    private String postMainImage;

    @QueryProjection
    public PostListRespDto(Long postId, Long userId, String username, String name, String profileImage, Write write, String amount, Category category, PostState state, String postMainImage) {
        this.postId = postId;
        this.userRespDto = new UserRespDto(userId, username, name, profileImage ,null);
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.postMainImage = postMainImage;
    }


    @Builder
    public PostListRespDto(Long postId, UserRespDto userRespDto, Write write, String amount, Category category, PostState state, String postMainImage) {
        this.postId = postId;
        this.userRespDto = userRespDto;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.postMainImage = postMainImage;
    }
}
