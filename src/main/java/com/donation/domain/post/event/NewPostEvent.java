package com.donation.domain.post.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NewPostEvent {

    private Long userId;
    private Long postId;

    @Builder
    public NewPostEvent(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
