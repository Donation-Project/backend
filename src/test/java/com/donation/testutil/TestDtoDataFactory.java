package com.donation.testutil;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.testutil.TestEntityDataFactory.metamask;

public class TestDtoDataFactory {

    public static UserJoinReqDto createUserJoinReqDto(){
        return UserJoinReqDto.builder()
                .email("user@naver.com")
                .name("name")
                .password("password")
                .metamask(metamask)
                .build();
    }

    public static UserLoginReqDto createUserLoginReqDto(String username, String password){
        return UserLoginReqDto.builder()
                .email(username)
                .password(password)
                .build();
    }

    public static PostSaveReqDto createPostSaveReqDto(String title, String content){
        return PostSaveReqDto.builder()
                .title(title)
                .content(content)
                .amount("1")
                .category(ETC)
                .build();
    }

    public static PostUpdateReqDto createPostUpdateReqDto(String title, String content){
        return PostUpdateReqDto.builder()
                .title(title)
                .content(content)
                .amount("1")
                .category(ETC)
                .build();
    }
}
