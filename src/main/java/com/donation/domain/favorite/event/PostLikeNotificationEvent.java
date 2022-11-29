package com.donation.domain.favorite.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeNotificationEvent {

    private Long toUserId;
    private Long postId;

    @Builder
    public PostLikeNotificationEvent(Long toUserId, Long postId) {
        this.toUserId = toUserId;
        this.postId = postId;
    }
}
