package com.donation.common;

import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;

public class DonationFixtures {


    /* 기부 생성 */
    public static DonationSaveReqDto 기부_생성_DTO(Long userId, Long postId, String amount){
        return DonationSaveReqDto.builder()
                .userId(userId)
                .postId(postId)
                .amount(amount)
                .build();
    }

    public static Donation createDonation(User user, Post post, String amount){
        return  Donation.builder()
                .user(user)
                .post(post)
                .amount(amount)
                .build();
    }
}
