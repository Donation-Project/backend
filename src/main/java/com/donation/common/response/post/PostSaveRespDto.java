package com.donation.common.response.post;

import com.donation.common.response.user.UserRespDto;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.PostDetailImage;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostSaveRespDto {
    private Long postId;
    private UserRespDto userRespDto;
    private Write write;
    private String amount;
    private Category category;
    private PostState state;
    private List<String> postDetailImages;

    @Builder
    public PostSaveRespDto(Long postId, UserRespDto userRespDto, Write write, String amount, Category category, PostState state, List<String> postDetailImages) {
        this.postId = postId;
        this.userRespDto = userRespDto;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.postDetailImages = postDetailImages;
    }

    public static PostSaveRespDto toDto(Post post, User user){
        return PostSaveRespDto.builder()
                .postId(post.getId())
                .userRespDto(new UserRespDto(user))
                .write(new Write(post))
                .amount(post.getAmount())
                .state(post.getState())
                .postDetailImages(
                        post.getPostDetailImages().stream()
                        .map(PostDetailImage::getImagePath)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
