package com.donation.common.request.favorites;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class LikeSaveAndCancelReqDto {

    @NotBlank
    private Long userId;
    @NotBlank
    private Long postId;

    @Builder
    public LikeSaveAndCancelReqDto(final Long userId, final Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
