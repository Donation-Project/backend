package com.donation.domain;

import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.PostDetailImage;
import com.donation.exception.DonationNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.PostFixtures.*;
import static com.donation.domain.enums.PostState.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PostTest {

    @Test
    @DisplayName("게시물을 생성한다.")
    void 게시물을_생성한다(){
        //given & when & then
        assertDoesNotThrow(() -> createPost());
    }

    @Test
    @DisplayName("게시물을 생성할때 이미지도 같이 저장한다")
    void 게시물을_생성할떄_이미지도_같이_저장한다(){
        //given
        Post 게시물 = createPost();
        PostDetailImage 게시물이미지 = PostDetailImage.builder()
                .imagePath(일반_게시물_이미지_주소).build();

        //when
        게시물.addPostImage(게시물이미지);

        //then
        Assertions.assertAll(() ->{
            assertDoesNotThrow(() -> 게시물);
            assertThat(게시물.getPostDetailImages()).contains(게시물이미지);
        });
    }

    @Test
    @DisplayName("게시물을 생성할때 좋아요도 같이 저장한다")
    void 게시물을_생성할때_좋아요도_같이_저장한다(){
        //given
        Post 게시물 = createPost();
        Favorites 좋아요 = Favorites.builder().build();

        //when
        게시물.addFavorite(좋아요);

        //then
        Assertions.assertAll(() ->{
            assertDoesNotThrow(() -> 게시물);
            assertThat(게시물.getFavorites()).contains(좋아요);
            assertThat(게시물.getFavorites().size()).isEqualTo(1);
        });
    }

    @Test
    @DisplayName("게시물 내용 수정 성공")
    void 게시물_내용_수정_성공(){
        //given
        Post 게시물 = createPost();

        //when
        게시물.changePost(게시물_수정_DTO());

        //then
        Assertions.assertAll(() ->{
            assertThat(게시물.getWrite().getTitle()).isEqualTo(게시물_수정_제목);
            assertThat(게시물.getWrite().getContent()).isEqualTo(게시물_수정_내용);
            assertThat(게시물.getAmount()).isEqualTo(게시물_수정_기부금);
            assertThat(게시물.getCategory()).isEqualTo(게시물_수정_카테고리);
        });
    }

    @Test
    @DisplayName("게시물 상태 수정 성공")
    void 게시물_상태_수정_성공(){
        //given
        Post 게시물 = createPost();

        //when
        게시물.confirm(COMPLETION);

        //then
        assertThat(게시물.getState()).isEqualTo(COMPLETION);
    }

    @Test
    @DisplayName("기부됨에 따라 현재 모인금액이 증가한다.")
    void 기부됨에_따라_현재_모인금액이_증가한다(){
        //given
        float amount = 10.2f;
        Post 게시물 = createPost();

        //when
        게시물.increase(amount);

        //then
        assertThat(게시물.getCurrentAmount()).isEqualTo(amount);
    }

    @Test
    @DisplayName("기부금액이 최대금액을 넘어선 경우 오류를 던진다.")
    void 기부금액이_최대금액을_넘어선_경우_오류를_던진다(){
        float amount = 3001;
        Post 게시물 = createPost();

        //when & then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> 게시물.increase(amount))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("목표금액보다 금액이 커질 수 없습니다.");
    }
}
