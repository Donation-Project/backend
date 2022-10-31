package com.donation.common.response.comment;

import com.donation.domain.entites.Comment;
import com.donation.domain.entites.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponse {

    private Long id;
    private String name;
    private String profileImage;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public ReplyResponse(final Long id, final String name,final String profileImage,final String content,final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static ReplyResponse of(Comment comment, User user){
        return ReplyResponse.builder()
                .id(comment.getId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .content(comment.getMessage().getValue())
                .createdAt(comment.getCreateAt())
                .build();
    }
}
