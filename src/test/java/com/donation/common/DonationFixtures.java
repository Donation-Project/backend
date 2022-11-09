package com.donation.common;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.PostFixtures.일반_게시물_카테고리;
import static com.donation.common.UserFixtures.일반_사용자_이름;

public class DonationFixtures {

    public static String 후원_받는_사람 = "beneficiary@naver.com";
    public static String 후원자 = "sponsor@naver.com";

    public static String 후원금액 = "10.1";

    /* 기부 생성 */
    public static DonationSaveReqDto 기부_생성_DTO(Long userId, Long postId, String amount){
        return DonationSaveReqDto.builder()
                .userId(userId)
                .postId(postId)
                .amount(amount)
                .build();
    }

    public static DonationFilterReqDto 기부_전체검색_DTO(){
        return DonationFilterReqDto.builder()
                .username(일반_사용자_이름)
                .category(일반_게시물_카테고리)
                .build();
    }
    public static Donation createDonation(User user, Post post, String amount){
        return  Donation.builder()
                .user(user)
                .post(post)
                .amount(amount)
                .build();
    }
    public static List<Donation> createDonationList(int startNum, int lastNum, User user,Post post){
        return IntStream.range(startNum, lastNum)
                .mapToObj(i -> createDonation(user,post,String.valueOf(i)))
                .collect(Collectors.toList());
    }
}
