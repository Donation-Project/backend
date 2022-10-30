package com.donation.common.request.favorites;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeSaveAndCancelReqDto {

    private Long userId;

    private Long postId;

    @Builder
    public LikeSaveAndCancelReqDto(final Long userId, final Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
