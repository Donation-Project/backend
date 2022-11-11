package com.donation.presentation;

import com.donation.domain.reviews.application.ReviewService;
import com.donation.domain.reviews.dto.ReviewReqDto;
import com.donation.domain.reviews.dto.ReviewRespDto;
import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.donation.infrastructure.common.CommonResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> create(@LoginInfo LoginMember loginMember,
                                    @PathVariable(name = "id") Long postId,
                                    @RequestBody ReviewReqDto reviewReqDto){
        Long reviewId = reviewService.save(loginMember, postId, reviewReqDto);
        return ResponseEntity.created(URI.create(String.format("/api/post/%d/reviews", reviewId))).build();
    }

    @PutMapping("/{id}/reviews")
    public ResponseEntity<?> changeContent(@LoginInfo LoginMember loginMember,
                                           @PathVariable(name = "id") Long postId,
                                           @RequestBody ReviewReqDto reviewReqDto){
        reviewService.changeContent(loginMember, postId, reviewReqDto);
        return ResponseEntity.ok(success());
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getReview(@PathVariable(name = "id") Long postId){
        ReviewRespDto review = reviewService.getReview(postId);
        return ResponseEntity.ok(success(review));
    }

    @DeleteMapping("/{id}/reviews")
    public ResponseEntity<?> delete(@LoginInfo LoginMember loginMember,
                                    @PathVariable(name = "id") Long postId){
        reviewService.delete(loginMember, postId);
        return ResponseEntity.ok(success());
    }
}
