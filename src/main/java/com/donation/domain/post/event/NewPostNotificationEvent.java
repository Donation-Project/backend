package com.donation.domain.post.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostNotificationEvent {

    private Long userId;
    private Long postId;

    @Builder
    public NewPostNotificationEvent(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
