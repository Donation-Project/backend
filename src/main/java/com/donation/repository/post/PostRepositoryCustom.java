package com.donation.repository.post;

import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PostRepositoryCustom {
    public Slice<PostRespDto> findPageableAll(Pageable pageable);

    public Optional<PostFindRespDto> findByUserIdOrRoleAdmin(Long postId, Long userId);
}
