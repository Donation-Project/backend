package com.donation.common;

import com.donation.domain.post.entity.Post;
import com.donation.domain.reviews.dto.ReviewReqDto;
import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.user.entity.User;
import com.donation.infrastructure.embed.Write;

public class ReviewFixture {

    public static String 감사글_제목 = "감사글 제목 입니다.";
    public static String 감사글_내용 = "감사글 내용 입니다.";
    public static String 수정된_감사글_제목 = "수정된 제목 입니다.";
    public static String 수정된_감사글_내용 = "수정된 감사글 내용입니다.";
    public static Write 수정된_감사글 = new Write(수정된_감사글_제목, 수정된_감사글_내용);
    public static Write 감사글 = new Write(감사글_제목, 감사글_내용);


    public static ReviewReqDto 감사글(){
        return ReviewReqDto.builder()
                .write(감사글)
                .build();
    }

    public static ReviewReqDto 수정된_감사글(){
        return ReviewReqDto.builder()
                .write(수정된_감사글)
                .build();
    }

    public static Reviews createReview(User user, Post post){
        return Reviews.builder()
                .user(user)
                .post(post)
                .write(감사글)
                .build();
    }
}
