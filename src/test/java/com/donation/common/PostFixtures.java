package com.donation.common;

import com.donation.domain.favorite.entity.Favorites;
import com.donation.domain.post.dto.*;
import com.donation.domain.post.entity.Category;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.entity.PostDetailImage;
import com.donation.domain.post.entity.PostState;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.domain.user.entity.User;
import com.donation.infrastructure.embed.Write;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.UserFixtures.createUser;
import static com.donation.common.UserFixtures.새로운_이미지;
import static com.donation.domain.post.entity.Category.ETC;
import static com.donation.domain.post.entity.Category.병원비;
import static com.donation.domain.post.entity.PostState.APPROVAL;
import static org.jeasy.random.FieldPredicates.*;

public class PostFixtures {

    public static String 일반_게시물_기부금 = "100";
    public static float 일반_게시물_현재_모금액 = 30;
    public static String 일반_게시물_제목 = "일반 게시물 제목";
    public static String 일반_게시물_내용 = "일반 게시물 내용";
    public static Write 일반_게시물_게시글 = new Write(일반_게시물_제목, 일반_게시물_내용);
    public static Category 일반_게시물_카테고리 = ETC;
    public static PostState 일반_게시물_상태 = APPROVAL;
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
    public static PostSaveRespDto 게시물_생성_응답(){
        return PostSaveRespDto.builder()
                .postId(1L)
                .userRespDto(UserRespDto.of(createUser(1L)))
                .amount(일반_게시물_기부금)
                .write(일반_게시물_게시글)
                .postDetailImages(List.of(일반_게시물_이미지_주소))
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .build();
    }

    /* 게시물 단건조회 응답*/
    public static PostFindRespDto 게시물_단건조회_응답(){
        return PostFindRespDto.builder()
                .postId(1L)
                .userRespDto(UserRespDto.of(createUser(1L)))
                .amount(일반_게시물_기부금)
                .currentAmount(일반_게시물_현재_모금액)
                .write(일반_게시물_게시글)
                .postDetailImages(List.of(일반_게시물_이미지_주소))
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .favoriteCount(120)
                .build();
    }

    /*게시물 전체조회 응답*/
    public static PostListRespDto 게시물_전체조회_응답(Long id, User user){
        return PostListRespDto.builder()
                .postId(id)
                .userId(user.getId())
                .username(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .amount(일반_게시물_기부금)
                .currentAmount(일반_게시물_현재_모금액)
                .write(new Write(일반_게시물_제목 + id, 일반_게시물_내용 + id))
                .state(일반_게시물_상태)
                .category(일반_게시물_카테고리)
                .postMainImage(일반_게시물_이미지_주소 + "/" + id)
                .build();
    }

    /* 게시물 수정 */
    public static String 게시물_수정_제목 = "게시물 수정 제목";
    public static String 게시물_수정_내용 = "게시물 수정 내용";
    public static String 게시물_수정_기부금 = "150";
    public static Category 게시물_수정_카테고리 = 병원비;
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

    public static Post createPost(Long id){
        return Post.builder()
                .id(id)
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

    public static EasyRandom createRandomPost(User user){
        var idPredicate = named("id")
                .and(ofType(Long.class))
                .and(inClass(Post.class));

        var userPredicate = named("user")
                .and(ofType(User.class))
                .and(inClass(Post.class));

        var amountPredicate = named("amount")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var currentAmountPredicate = named("currentAmount")
                .and(ofType(String.class))
                .and(inClass(Post.class));

        var categoryPredicate = named("category")
                .and(ofType(Category.class))
                .and(inClass(Post.class));

        var statePredicate = named("state")
                .and(ofType(PostState.class))
                .and(inClass(Post.class));

        var postDetailImagesPredicate = named("postDetailImages")
                .and(ofType(PostDetailImage.class))
                .and(inClass(Post.class));

        var favoritePredicate = named("favorites")
                .and(ofType(Favorites.class))
                .and(inClass(Post.class));


        var param = new EasyRandomParameters()
                .seed(1)
                .excludeField(idPredicate)
                .excludeField(postDetailImagesPredicate)
                .excludeField(favoritePredicate)
                .randomize(userPredicate, () -> user)
                .randomize(amountPredicate, () -> "100")
                .randomize(currentAmountPredicate, () -> 0.0f)
                .randomize(categoryPredicate, () -> ETC)
                .randomize(statePredicate, () -> APPROVAL);

        return new EasyRandom(param);
    }
}
