package com.donation.domain.post.service;

import com.donation.presentation.auth.LoginMember;
import com.donation.domain.post.dto.PostSaveReqDto;
import com.donation.domain.post.dto.PostUpdateReqDto;
import com.donation.domain.post.dto.PostFindRespDto;
import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.dto.PostSaveRespDto;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.entity.PostDetailImage;
import com.donation.domain.user.entity.User;
import com.donation.domain.post.entity.PostState;
import com.donation.domain.comment.repository.CommentRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import com.donation.infrastructure.support.PageCustom;
import com.donation.infrastructure.Image.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
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
            post.addPostImage(PostDetailImage.of(awsS3Service.upload(image)));
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

    public PageCustom<PostListRespDto> getList(Pageable pageable, PostState... states) {
        return postRepository.getPageList(pageable, states);

    }

    public PageCustom<PostListRespDto> getUserIdList(LoginMember loginMember, Pageable pageable){
        return postRepository.getUserIdPageList(loginMember.getId(), pageable) ;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increase(Long id, float amount){
        Post post = postRepository.findByIdWithLock(id);
        post.increase(amount);
        postRepository.saveAndFlush(post);
    }

}
