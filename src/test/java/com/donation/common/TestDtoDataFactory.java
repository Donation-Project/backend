package com.donation.common;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;

import static com.donation.domain.enums.Category.ETC;

public class TestDtoDataFactory {


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
