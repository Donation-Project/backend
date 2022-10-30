package com.donation.common;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.UserFixtures.*;

public class PostFixtures {

    public static String 일반_게시물_기부금 = "100";
    public static String 일반_게시물_제목 = "일반 게시물 제목";
    public static String 일반_게시물_내용 = "일반 게시물 내용";
    public static Write 일반_게시물_게시글 = new Write(일반_게시물_제목, 일반_게시물_내용);
    public static Category 일반_게시물_카테고리 = Category.ETC;
    public static PostState 일반_게시물_상태 = PostState.APPROVAL;
    public static String 일반_게시물_이미지_주소 = "https://avatars.githubusercontent.com/u/106054507?v=4";

    /* 게시물 생성 */
    public static PostSaveReqDto 게시물_생성_DTO(){
        return PostSaveReqDto.builder()
                .title(일반_게시물_제목)
                .content(일반_게시물_내용)
                .amount(일반_게시물_기부금)
                .category(일반_게시물_카테고리)
                .image(새로운_이미지)
                .build();
    }

    /* 게시물 수정 */
    public static String 게시물_수정_제목 = "게시물 수정 제목";
    public static String 게시물_수정_내용 = "게시물 수정 내용";
    public static String 게시물_수정_기부금 = "150";
    public static Category 게시물_수정_카테고리 = Category.병원비;
    public static PostUpdateReqDto 게시물_수정_DTO(){
        return PostUpdateReqDto.builder()
                .title(게시물_수정_제목)
                .content(게시물_수정_내용)
                .amount(게시물_수정_기부금)
                .category(게시물_수정_카테고리)
                .build();
    }

    public static Post createPost(){
        return Post.builder()
                .write(일반_게시물_게시글)
                .amount(일반_게시물_기부금)
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .build();
    }

    public static Post createPost(User user){
        return Post.builder()
                .user(user)
                .write(일반_게시물_게시글)
                .amount(일반_게시물_기부금)
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .build();
    }

    public static Post createPost(User user, String title, String content){
        return Post.builder()
                .user(user)
                .write(new Write(title, content))
                .amount(일반_게시물_기부금)
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .build();
    }


    public static List<Post> creatPostList(int startNum, int lastNum, User user){
        return IntStream.range(startNum, lastNum)
                .mapToObj(i -> createPost(user,"title" + i,"content" + i))
                .collect(Collectors.toList());
    }
}
