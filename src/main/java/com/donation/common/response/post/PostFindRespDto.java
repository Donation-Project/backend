package com.donation.common.response.post;

import com.donation.common.response.user.UserRespDto;
import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
public class PostFindRespDto {
    private Long postId;

    private UserRespDto userRespDto;

    private Write write;

    private String amount;

    private Category category;

    private PostState state;

    //이미지 정보 추가 기입
    private List<String> postDetailImages;

    private Long favoriteCount;

    @QueryProjection
<<<<<<< HEAD
    public PostFindRespDto(Long postId, Long userId, String email, String name, String profileImage, String metamask,Write write, float amount, Category category, PostState state) {
=======
    public PostFindRespDto(Long postId, Long userId, String username, String name, String profileImage, String metamask,Write write, String amount, Category category, PostState state) {
>>>>>>> bf05c1c1ae01812c4cc835ed9ab49973c99b11c3
        this.postId = postId;
        this.userRespDto = new UserRespDto(userId, email, name, profileImage, metamask);
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
    }

    public void setPostDetailImages(List<String> postDetailImages){
        this.postDetailImages = postDetailImages;
    }

    public void setFavoriteCount(Long favoriteCount){
        this.favoriteCount = favoriteCount;
    }
}
