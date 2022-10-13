package com.donation.repository;

import com.donation.common.response.post.PostRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {
    public Slice<PostRespDto> findPageableAll(Pageable pageable);
}
