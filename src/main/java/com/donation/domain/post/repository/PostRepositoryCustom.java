package com.donation.domain.post.repository;

import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.entity.PostState;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostListRespDto> getPageDtoAll(Pageable pageable,PostState... postState);

    List<PostListRespDto> getUserIdPageDtoList(Long userId, Pageable pageable);
}
