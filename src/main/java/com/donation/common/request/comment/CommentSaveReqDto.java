package com.donation.common.request.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CommentSaveReqDto {

    @NotBlank(message = "댓글은 1자 이상 100자 이하여야 합니다.")
    String message;

    @Builder
    public CommentSaveReqDto(final String message) {
        this.message = message;
    }
}
