package com.donation.domain.reviews.application;

import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.reviews.dto.ReviewRespDto;
import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.reviews.repository.ReviewRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.infrastructure.embed.Write;
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
    public void save(LoginMember loginMember, Long postId, Write write){
        User user = userRepository.getById(loginMember.getId());
        Post post = postRepository.getById(postId);
        reviewRepository.save(Reviews.of(user, post, write));
    }

    @Transactional
    public void changeWrite(LoginMember loginMember, Long postId, Write changeWrite){
        Reviews reviews = reviewRepository.getByPostId(postId);
        reviews.validateOwner(loginMember.getId());
        reviews.changeWrite(changeWrite);
    }

    public ReviewRespDto getReview(Long postId) {
        Reviews review = reviewRepository.getByPostId(postId);
        return ReviewRespDto.of(review);
    }

    @Transactional
    public void delete(LoginMember loginMember, Long id){
        Reviews review = reviewRepository.getById(id);
        review.validateOwner(loginMember.getId());
        reviewRepository.delete(review);
    }
}
