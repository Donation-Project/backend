package com.donation.service.post;

import com.donation.auth.LoginMember;
import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.PostDetailImage;
import com.donation.domain.entites.User;
import com.donation.domain.enums.PostState;
import com.donation.repository.comment.CommentRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.donation.repository.utils.PageCustom;
import com.donation.service.s3.AwsS3Service;
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

        postRepository.getById(id).changePost(updateReqDto);
    }

    @Transactional
    public void confirm(PostState postState, Long id) {
        postRepository.getById(id).confirm(postState);
    }

    @Transactional
    public void delete(LoginMember loginMember) {
        Post post = postRepository.getById(loginMember.getId());
        postRepository.delete(post);
        commentRepository.deleteByPost(post);
    }

    public PageCustom<PostListRespDto> getList(Pageable pageable, PostState... states) {
        return postRepository.getPageList(pageable, states);

    }

    public PageCustom<PostListRespDto> getUserIdList(Long id, Pageable pageable){
        return postRepository.getUserIdPageList(id, pageable) ;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increase(Long id, float amount){
        Post post = postRepository.findByIdWithLock(id);
        post.increase(amount);
        postRepository.saveAndFlush(post);
    }

}
