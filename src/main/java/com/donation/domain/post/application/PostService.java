package com.donation.domain.post.application;

import com.donation.domain.comment.repository.CommentRepository;
import com.donation.domain.post.dto.*;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.entity.PostDetailImage;
import com.donation.domain.post.entity.PostState;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.domain.post.application.Image.ImageService;
import com.donation.infrastructure.util.CursorRequest;
import com.donation.infrastructure.util.PageCursor;
import com.donation.presentation.auth.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final CommentRepository commentRepository;

    @Transactional
    public PostSaveRespDto createPost(PostSaveReqDto postSaveReqDto, LoginMember loginMember) {
        User user = userRepository.getById(loginMember.getId());
        Post post = postRepository.save(validateSave(postSaveReqDto, postSaveReqDto.getImage(), user));
        return PostSaveRespDto.of(post, user);
    }
    private Post validateSave(PostSaveReqDto postSaveReqDto, String image, User user) {
        Post post = postSaveReqDto.toPost(user);
        if (StringUtils.hasText(image))
            post.addPostImage(PostDetailImage.of(imageService.upload(image)));
        return post;
    }

    public PostFindRespDto findById(Long postId) {
        postRepository.validateExistsById(postId);
        return PostFindRespDto.of(postRepository.findDetailById(postId));
    }

    @Transactional
    public void update(PostUpdateReqDto updateReqDto, LoginMember loginMember, Long id) {
        Post post = postRepository.getById(id);
        post.validateOwner(loginMember.getId());
        post.changePost(updateReqDto);
    }

    @Transactional
    public void confirm(PostState postState, Long id) {
        postRepository.getById(id).confirm(postState);
    }

    @Transactional
    public void delete(Long id, LoginMember loginMember) {
        Post post = postRepository.getById(id);
        post.validateOwner(loginMember.getId());
        postRepository.delete(post);
        commentRepository.deleteByPost(post);
    }

    public PageCursor<PostListRespDto> getList(CursorRequest cursorRequest, PostState... states) {
        List<PostListRespDto> posts = postRepository.findDtoAllByIdLessThanAndStateInOrderByIdDesc(cursorRequest, states);
        Long nextKey = getNextKey(posts);

        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public PageCursor<PostListRespDto> getList(LoginMember loginMember, CursorRequest cursorRequest){
        List<PostListRespDto> posts = postRepository.findDtoAllByUserIdOrderByIdDesc(loginMember.getId(), cursorRequest);
        Long nextKey = getNextKey(posts);

        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    private static long getNextKey(List<PostListRespDto> posts) {
        return posts.stream()
                .mapToLong(PostListRespDto::getPostId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increase(Long id, float amount){
        Post post = postRepository.findByIdWithLock(id);
        post.increase(amount);
        postRepository.saveAndFlush(post);
    }

}
