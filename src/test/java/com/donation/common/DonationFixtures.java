package com.donation.common;

import com.donation.presentation.auth.LoginMember;
import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.entity.Donation;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.PostFixtures.*;
import static com.donation.common.UserFixtures.createUser;
import static com.donation.common.UserFixtures.일반_사용자_이름;

public class DonationFixtures {

    public static String 후원_받는_사람 = "beneficiary@naver.com";
    public static String 후원자 = "sponsor@naver.com";

    public static String 후원금액 = "100";

    /* 기부 생성 */
    public static DonationSaveReqDto 기부_생성_DTO(Long userId, Long postId, String amount){
        return DonationSaveReqDto.builder()
                .userId(userId)
                .postId(postId)
                .amount(amount)
                .build();
    }

    /*게시물 전체조회 응답*/
    public static DonationFindByFilterRespDto 기부_전체조회_응답(Long id,Post post, User user){
        return DonationFindByFilterRespDto.builder()
                .donateId(id)
                .postId(post.getId())
                .userId(user.getId())
                .title(post.getWrite().getTitle())
                .amount(일반_게시물_기부금)
                .grossAmount(일반_게시물_현재_모금액)
                .fromUser(user.getName())
                .toUser(post.getUser().getName())
                .category(일반_게시물_카테고리)
                .build();
    }

    public static DonationFindRespDto 유저에_대한_기부_응답(Post post, User user){
        return DonationFindRespDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .title(post.getWrite().getTitle())
                .amount(일반_게시물_기부금)
                .grossAmount(일반_게시물_현재_모금액)
                .category(일반_게시물_카테고리)
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
