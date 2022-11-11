package com.donation.domain.reviews.entity;

import com.donation.global.exception.DonationInvalidateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.ReviewFixture.createReview;
import static com.donation.common.UserFixtures.createUser;
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
        Assertions.assertThatThrownBy(() -> review.validateOwner(expected))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("작성자만 권한이 있습니다.");
    }

}