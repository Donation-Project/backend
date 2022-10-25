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
import com.donation.repository.favorite.FavoriteRedisRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.postdetailimage.PostDetailImageRepository;
import com.donation.service.s3.AwsS3Service;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

import static com.donation.domain.enums.PostState.DELETE;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostDetailImageRepository postDetailImageRepository;
    private final FavoriteRedisRepository favoriteRedisRepository;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
    }

    public PostSaveRespDto save(PostSaveReqDto postSaveReqDto, Long userId) {
        User user = userService.getUser(userId);
        Post post = postRepository.save(postSaveValidation(postSaveReqDto, postSaveReqDto.getImages(), user));
        return PostSaveRespDto.toDto(post, user);
    }

    private Post postSaveValidation(PostSaveReqDto postSaveReqDto, List<String> images, User user) {
        Post post = postSaveReqDto.toPost(user);
        if (!images.isEmpty())
            addPostDetailImage(images, post);
        return post;
    }

    private void addPostDetailImage(List<String> images, Post post) {
        for (String image : images) {
            if (image.isEmpty())
                continue;

            byte[] imageDecode = Base64.getMimeDecoder().decode(image.substring(image.indexOf(",") + 1));
            post.addPostImage(PostDetailImage.of(awsS3Service.upload(imageDecode)));
        }
    }

    public void update(PostUpdateReqDto updateReqDto, Long id) {
        getPost(id).update(updateReqDto);
    }

    public void confirm(PostState postState, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
        post.confirm(postState);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
        postRepository.delete(post);
    }

    public PostFindRespDto findById(Long postId) {
        return validateWithDto(postId);
    }

    private PostFindRespDto validateWithDto(Long postId){
        PostFindRespDto findPostDto = postRepository.findDetailPostById(postId)
                .orElseThrow(() -> new DonationNotFoundException("포스트를 찾을수 없습니다."));
        List<String> imagePath = postDetailImageRepository.findImagePath(findPostDto.getPostId());
        Long count = favoriteRedisRepository.count(postId);
        findPostDto.setPostDetailImages(imagePath);
        findPostDto.setFavoriteCount(count);
        return findPostDto;
    }

    public Slice<PostListRespDto> getList(Pageable pageable, PostState... states) {
        return postRepository.findDetailPostAll(pageable, states);

    }

    @Scheduled(cron = "0 0 0 * * *")
    public void postStateIsDeleteAnd7DaysOver() {
        LocalDateTime localDateTime = LocalDateTime
                .now()
                .minus(7, ChronoUnit.DAYS);
        postRepository.deleteAll(postRepository.findAllByUpdateAdLessThanEqualAndState(localDateTime, DELETE));
    }
}
