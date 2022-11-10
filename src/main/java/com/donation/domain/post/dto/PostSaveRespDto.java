package com.donation.domain.post.dto;

import com.donation.domain.user.dto.UserRespDto;
import com.donation.infrastructure.embed.Write;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.entity.PostDetailImage;
import com.donation.domain.user.entity.User;
import com.donation.domain.post.entity.Category;
import com.donation.domain.post.entity.PostState;
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

    public static PostSaveRespDto of(Post post, User user){
        return PostSaveRespDto.builder()
                .postId(post.getId())
                .userRespDto(UserRespDto.of(user))
                .write(post.getWrite())
                .amount(post.getAmount())
                .state(post.getState())
                .category(post.getCategory())
                .postDetailImages(
                        post.getPostDetailImages().stream()
                        .map(PostDetailImage::getImagePath)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
