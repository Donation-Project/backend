package com.donation.repository.post;

import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface PostRepositoryCustom {

    /**
     * 단건 조회
     */
    Optional<PostFindRespDto> findDetailPostById(Long postId);

    public Slice<PostListRespDto> findDetailPostAll(Pageable pageable);


    /**
     * 회원 아이디로 리스트 조회
     */
    public Slice<PostListRespDto> findAllUserId(Long userId, Pageable pageable);


//    public Slice<PostListRespDto> findSearchList(PostSearchReqDto postSearchReqDto, Pageable pageable);

}
