package com.donation.common.response.comment;

import com.donation.domain.entites.Comment;
import com.donation.domain.entites.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String name;
    private String profileImage;
    private String content;
    private LocalDateTime createdAt;
    private List<ReplyResponse> replies;

    @Builder
    public CommentResponse(final Long id,final String name,final String profileImage,final String content,final LocalDateTime createdAt, final List<ReplyResponse> replies) {
        this.id = id;
        this.name = name;
        this.profileImage = profileImage;
        this.content = content;
        this.createdAt = createdAt;
        this.replies = replies;
    }

    public static CommentResponse of(final Comment comment, final User user, List<ReplyResponse> replies){
        return CommentResponse.builder()
                .id(comment.getId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .content(comment.getMessage().getValue())
                .createdAt(comment.getCreateAt())
                .replies(replies)
                .build();
    }
}
