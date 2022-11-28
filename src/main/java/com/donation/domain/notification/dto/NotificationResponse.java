package com.donation.domain.notification.dto;

import com.donation.domain.notification.entity.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String message;
    private Long postId;
    private Long commentId;

    @Builder
    public NotificationResponse(Long id, String message, Long postId, Long commentId) {
        this.id = id;
        this.message = message;
        this.postId = postId;
        this.commentId = commentId;
    }

    public static NotificationResponse of(Notification notification){
        return NotificationResponse.builder()
                .id(notification.getId())
                .message(notification.getType().toMessage())
                .postId(notification.getPostId())
                .commentId(notification.getCommentId())
                .build();
    }
}
