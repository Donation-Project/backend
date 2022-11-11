package com.donation.domain.reviews.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.Write;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.PostFixtures.*;
import static com.donation.common.ReviewFixture.createReview;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReviewsTest {

    @Test
    @DisplayName("정상적으로 리뷰 객체를 생성한다.")
    void 정상적으로_리뷰_객체를_생성한다(){
        //given & when & then
        assertDoesNotThrow(() -> createReview(createUser(), createPost()));
    }

    @Test
    @DisplayName("작성자가 아닐때 해당 게시글에 대한 수정 또는 삭제가 필요한 경우 오류 던진다.")
    void 작성자가_아닐때_해당_게시글에_대한_수정_또는_삭제가_필요한_경우_오류를_던진다(){
        //given
        Reviews review = createReview(createUser(), createPost());
        Long expected = 0L;

        //& when & then
        assertThatThrownBy(() -> review.validateOwner(expected))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("작성자만 권한이 있습니다.");
    }

    @Test
    @DisplayName("리뷰의 내용을 수정한다.")
    void 리뷰의_내용을_수정한다(){
        //given
        Reviews review = createReview(createUser(), createPost());
        Write 수정내용 = new Write(게시물_수정_제목, 게시물_수정_내용);

        //when
        review.changeContent(수정내용);

        //then
        assertAll(() -> {
            assertThat(review.getWrite().getTitle()).isEqualTo(게시물_수정_제목);
            assertThat(review.getWrite().getContent()).isEqualTo(게시물_수정_내용);
        });
    }
}