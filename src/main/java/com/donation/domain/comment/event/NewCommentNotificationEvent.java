package com.donation.domain.comment.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentNotificationEvent {

    private Long toUserId;
    private Long fromUserId;
    private Long postId;
    private Long commentId;

    @Builder
    public NewCommentNotificationEvent(Long toUserId, Long fromUserId, Long postId, Long commentId) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.postId = postId;
        this.commentId = commentId;
    }
}
