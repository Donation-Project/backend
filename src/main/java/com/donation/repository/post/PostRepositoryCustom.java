package com.donation.repository.post;

import com.donation.common.response.post.PostListRespDto;
import com.donation.domain.enums.PostState;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListRespDto> getPageDtoAll(Pageable pageable,PostState... postState);

    List<PostListRespDto> getUserIdPageDtoList(Long userId, Pageable pageable);
}
