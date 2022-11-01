package com.donation.service.post;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final CommentRepository commentRepository;

    public PostSaveRespDto createPost(PostSaveReqDto postSaveReqDto, Long userId) {
        User user = userRepository.getById(userId);
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

    public void update(PostUpdateReqDto updateReqDto, Long id) {
        postRepository.getById(id).changePost(updateReqDto);
    }

    public void confirm(PostState postState, Long id) {
        postRepository.getById(id).confirm(postState);
    }

    public void delete(Long postId) {
        Post post = postRepository.getById(postId);
        postRepository.delete(post);
        commentRepository.deleteByPost(post);
    }

    public PageCustom<PostListRespDto> getList(Pageable pageable, PostState... states) {
        return postRepository.getPageList(pageable, states);

    }

    public PageCustom<PostListRespDto> getUserIdList(Long id, Pageable pageable){
        return postRepository.getUserIdPageList(id, pageable) ;
    }
}
