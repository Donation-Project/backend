package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.PostState;
import com.donation.repository.post.PostRepository;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.donation.domain.enums.PostState.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostSaveRespDto save(PostSaveReqDto postSaveReqDto, Long userid){
        User user = userService.get(userid);
        Post post = postRepository.save(postSaveReqDto.toPost(user));
        return PostSaveRespDto.toDto(post, user);
    }

    public void update(PostUpdateReqDto updateReqDto, Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        post.update(updateReqDto);
    }

    public void checkingPost(PostState postState, Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        post.checkPost(postState);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);
        postRepository.delete(post);
    }

    public PostFindRespDto findById(Long postId) {
        return postRepository.findDetailPostById(postId)
                .orElseThrow(IllegalArgumentException::new);
    }


    public Slice<PostListRespDto> getList(Pageable pageable) {
        return postRepository.findDetailPostAll(pageable, APPROVAL,COMPLETION);
    }

    public Slice<PostListRespDto> getAdminPostList(Pageable pageable,PostState... state) {
        return postRepository.findDetailPostAll(pageable, state);
    }


    public Slice<PostListRespDto> findAllUserId(Long userId, Pageable pageable){
        return postRepository.findAllUserId(userId, pageable);
    }

    public void postStateIsDeleteAnd7DaysOver(){
        LocalDateTime localDateTime = LocalDateTime
                .now()
                .minus(7, ChronoUnit.DAYS);

        postRepository.deleteAll(postRepository.findAllByUpdateAdLessThanEqualAndState(localDateTime, DELETE));
    }
}
