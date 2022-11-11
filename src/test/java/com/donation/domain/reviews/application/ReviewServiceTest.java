package com.donation.domain.reviews.application;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.reviews.dto.ReviewRespDto;
import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.reviews.repository.ReviewRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.global.exception.DonationDuplicateException;
import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.PostFixtures.*;
import static com.donation.common.ReviewFixture.*;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class ReviewServiceTest extends ServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("감사글이 정상적으로 작성된다.")
    void 리뷰가_정상적으로_작성된다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when
        Long expected = reviewService.save(회원검증(user.getId()), post.getId(), 감사글());
        Reviews actual = reviewRepository.getById(expected);

        //then
        assertThat(actual.getId()).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 해당 게시물에 작성된 감사글이 있으면 저장시 오류를 던진다.")
    void 이미_해당_게시물에_작성된_감사글이_있으면_저장시_오류를_던진다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        reviewService.save(회원검증(user.getId()), post.getId(), 감사글());

        //when & then
        assertThatThrownBy(() -> reviewService.save(회원검증(user.getId()), post.getId(), 감사글()))
                .isInstanceOf(DonationDuplicateException.class)
                .hasMessage("이미 작성한 글이 있습니다.");
    }

    @Test
    @DisplayName("게시물 작성자가 아닌 타인이 감사글을 저장요청시 오류를 던진다.")
    void 게시물_작성자가_아닌_타인이_감사글을_저장요청시_요류를_던진다(){
        //given
        User user = userRepository.save(createUser());
        User 타인 = userRepository.save(createUser("tain@naver.com"));
        Post post = postRepository.save(createPost(user));


        //when & then
        assertThatThrownBy(() -> reviewService.save(회원검증(타인.getId()), post.getId(), 감사글()))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("게시물의 작성자만 권한이 있습니다.");
    }

    @Test
    @DisplayName("저장된 감사글을 업데이트 한다.")
    void 저장된_리뷰글을_업데이트한다() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long reviewId = reviewService.save(회원검증(user.getId()), post.getId(), 감사글());

        //when
        reviewService.changeWrite(회원검증(user.getId()), post.getId(), 수정된_감사글());
        Reviews actual = reviewRepository.getById(reviewId);

        //then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(reviewId);
            assertThat(actual.getWrite().getTitle()).isEqualTo(수정된_감사글_제목);
            assertThat(actual.getWrite().getContent()).isEqualTo(수정된_감사글_내용);
        });
    }

    @Test
    @DisplayName("감사글을 업데이트시 작성자가 아니면 오류를 발생시킨다.")
    void 리뷰를_업데이트시_작성자가_아니면_오류를_발생시킨다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        reviewService.save(회원검증(user.getId()), post.getId(), 감사글());

        //when & then
        assertThatThrownBy(() -> reviewService.changeWrite(회원검증(0L), post.getId(), 수정된_감사글()))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("작성자만 권한이 있습니다.");
    }

    @Test
    @DisplayName("게시물 아이디를 통해 저장된 감사글이 조회한다.")
    void 게시물_아이디를_통해_저장된_리뷰를_조회한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long reviewId = reviewService.save(회원검증(user.getId()), post.getId(), 감사글());

        //when
        ReviewRespDto actual = reviewService.getReview(post.getId());

        //then
        assertAll(() -> {
            assertThat(actual.getUserRespDto().getId()).isEqualTo(user.getId());
            assertThat(actual.getReviewId()).isEqualTo(reviewId);
        });
    }

    @Test
    @DisplayName("게시물 아이디를 통해 저장된 감사글을 삭제한다.")
    void 게시물_아이디를_통해_저장된_감사글을_삭제한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long reviewId = reviewService.save(회원검증(user.getId()), post.getId(), 감사글());

        //when
        reviewService.delete(회원검증(user.getId()), post.getId());
        boolean actual = reviewRepository.existsById(reviewId);
        //then
        assertThat(actual).isFalse();
    }
}