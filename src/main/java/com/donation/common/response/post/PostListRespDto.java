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
    private float currentAmount;

    private Category category;

    private PostState state;

    private String postMainImage;

    @Builder
    @QueryProjection
    public PostListRespDto(final Long postId,final Long userId,final String username,final String name,final String profileImage,final Write write,
                           final String amount,final float currentAmount,final Category category,final PostState state,final String postMainImage) {
        this.postId = postId;
        this.userRespDto = new UserRespDto(userId, username, name, profileImage ,null);
        this.write = write;
        this.amount = amount;
        this.currentAmount = currentAmount;
        this.category = category;
        this.state = state;
        this.postMainImage = postMainImage;
    }
}
