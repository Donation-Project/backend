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
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.favorite.FavoriteRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.postdetailimage.PostDetailImageRepository;
import com.donation.repository.user.UserRepository;
import com.donation.service.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.donation.domain.enums.PostState.DELETE;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostDetailImageRepository postDetailImageRepository;
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
    }

    public PostSaveRespDto save(PostSaveReqDto postSaveReqDto, Long userId) {
        User user = userRepository.getById(userId);
        Post post = postRepository.save(postSaveValidation(postSaveReqDto, postSaveReqDto.getImage(), user));
        return PostSaveRespDto.toDto(post, user);
    }

    private Post postSaveValidation(PostSaveReqDto postSaveReqDto, String image, User user) {
        Post post = postSaveReqDto.toPost(user);
        if (StringUtils.hasText(image))
            post.addPostImage(PostDetailImage.of(awsS3Service.upload(image)));
        return post;
    }

    public void update(PostUpdateReqDto updateReqDto, Long id) {
        postRepository.getById(id)
                .changePost(updateReqDto);
    }

    public void confirm(PostState postState, Long id) {
        postRepository.getById(id)
                .confirm(postState);
    }

    public void delete(Long postId) {
        postRepository.delete(findById(postId));
    }

    public PostFindRespDto findDetailById(Long postId) {
        return validateWithDto(postId);
    }

    private PostFindRespDto validateWithDto(Long postId){
        PostFindRespDto findPostDto = postRepository.findDetailPostById(postId)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
        findPostDto.setPostDetailImages(postDetailImageRepository.findImagePath(findPostDto.getPostId()));
        findPostDto.setFavoriteCount(favoriteRepository.countFavoritesByPost_Id(postId));
        return findPostDto;
    }

    public Slice<PostListRespDto> getList(Pageable pageable, PostState... states) {
        return postRepository.findDetailPostAll(pageable, states);

    }

    public Slice<PostListRespDto> getMyPostList(Long id, Pageable pageable){
        return postRepository.findAllUserId(id, pageable);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void postStateIsDeleteAnd7DaysOver() {
        LocalDateTime localDateTime = LocalDateTime
                .now()
                .minus(7, ChronoUnit.DAYS);
        postRepository.deleteAll(postRepository.findAllByUpdateAtLessThanEqualAndState(localDateTime, DELETE));
    }
}
