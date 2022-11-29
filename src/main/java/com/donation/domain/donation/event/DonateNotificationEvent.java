package com.donation.domain.donation.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DonateNotificationEvent {

    private Long userId;
    private Long postId;

    @Builder
    public DonateNotificationEvent(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
