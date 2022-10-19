package com.donation.common.response.post;

import com.donation.common.response.user.UserRespDto;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostFindRespDto {
    private Long postId;

    private UserRespDto userRespDto;

    private Write write;

    private float amount;

    private Category category;

    private PostState state;

    //이미지 정보 추가 기입
    private List<String> postDetailImages;

    private Integer favoriteCount;

    @QueryProjection
    public PostFindRespDto(Long postId, Long userId, String username, String name, String profileImage, Write write, float amount, Category category, PostState state) {
        this.postId = postId;
        this.userRespDto = new UserRespDto(userId, username, name, profileImage);
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
    }


    @Builder
    public PostFindRespDto(Long postId, UserRespDto userRespDto, Write write, float amount, Category category, PostState state, List<String> postDetailImages, Integer favoriteCount) {
        this.postId = postId;
        this.userRespDto = userRespDto;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.postDetailImages = postDetailImages;
        this.favoriteCount = favoriteCount;
    }

    public static PostFindRespDto toDto(Post post){
        return PostFindRespDto.builder()
                .postId(post.getId())
                .userRespDto(new UserRespDto(post.getUser()))
                .write(new Write(post))
                .amount(post.getAmount())
                .state(post.getState())
                .postDetailImages(null)
                .favoriteCount(null)
                .build();
    }
}
