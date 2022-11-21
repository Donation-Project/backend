package com.donation.domain.post.repository;

import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.entity.PostState;
import com.donation.infrastructure.util.CursorRequest;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListRespDto> findDtoAllByIdLessThanAndStateInOrderByIdDesc(CursorRequest cursorRequest, PostState... states);
    List<PostListRespDto> findDtoAllByUserIdOrderByIdDesc(Long userId, CursorRequest cursorRequest);

}
