package com.donation.common;

import com.donation.common.request.favorites.LikeSaveAndCancelReqDto;
import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;

public class FavoriteFixtures {
    public static Long 포스트_아이디 = 1L;
    public static Long 회원_아이디 = 1L;
    public static String 좋아요_저장 = "SAVE";
    public static String 좋아요_취소 = "CANCEL";

    /* 좋아요 저장 또는 취소*/
    public static LikeSaveAndCancelReqDto 좋아요_DTO(Long 포스트_아이디, Long 회원_아이디){
        return LikeSaveAndCancelReqDto.builder()
                .postId(포스트_아이디)
                .userId(회원_아이디)
                .build();
    }

    public static Favorites createFavorites(User user, Post post) {
        return Favorites.builder()
                .user(user)
                .post(post)
                .build();
    }
}
