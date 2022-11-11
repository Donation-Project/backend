package com.donation.domain.reviews.application;

import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.reviews.dto.ReviewReqDto;
import com.donation.domain.reviews.dto.ReviewRespDto;
import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.reviews.repository.ReviewRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.presentation.auth.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(LoginMember loginMember, Long postId, ReviewReqDto reviewReqDto){
        User user = userRepository.getById(loginMember.getId());
        Post post = postRepository.getById(postId);

        validate(user, post);

        Reviews reviews = reviewRepository.save(Reviews.of(user, post, reviewReqDto.getWrite()));
        return reviews.getId();
    }

    private void validate(User user, Post post){
        post.validateOwner(user.getId());
        reviewRepository.validateExistsPostId(post.getId());
    }

    @Transactional
    public void changeContent(LoginMember loginMember, Long postId, ReviewReqDto reviewReqDto){
        Reviews reviews = reviewRepository.getByPostId(postId);
        reviews.validateOwner(loginMember.getId());
        reviews.changeContent(reviewReqDto.getWrite());
    }

    public ReviewRespDto getReview(Long postId) {
        Reviews review = reviewRepository.getByPostId(postId);
        return ReviewRespDto.of(review);
    }

    @Transactional
    public void delete(LoginMember loginMember, Long postId){
        Reviews review = reviewRepository.getByPostId(postId);
        review.validateOwner(loginMember.getId());
        reviewRepository.delete(review);
    }
}
