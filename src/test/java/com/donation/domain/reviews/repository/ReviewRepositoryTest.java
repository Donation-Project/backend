package com.donation.domain.reviews.repository;

import com.donation.common.utils.RepositoryTest;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.reviews.entity.Reviews;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.global.exception.DonationDuplicateException;
import com.donation.global.exception.DonationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.ReviewFixture.createReview;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class ReviewRepositoryTest extends RepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    @Test
    @DisplayName("게시물 아이디를 통해 작성한 글이 있는지 확인한다.")
    void 게시물_아이디를_통해_작성한_글이_있는지_확인한다() {
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when
        boolean actual = reviewRepository.existsByPostId(id);

        //then
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("이미 해당 게시물에 작성한 리뷰가 있다면 오류를 던진다.")
    void 이미_해당_게시물에_작성한_리뷰가_있다면_오류를_던진다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when
        reviewRepository.save(createReview(user, post));

        //then
        assertThatThrownBy(() -> reviewRepository.validateExistsPostId(post.getId()))
                .isInstanceOf(DonationDuplicateException.class)
                .hasMessage("이미 작성한 글이 있습니다.");
    }

    @Test
    @DisplayName("게시물 아이디로 리뷰를 조회할 수 있다.")
    void 게시물_아이디를_통해_게시물을_조회할_수_있다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when
        Reviews expected = reviewRepository.save(createReview(user, post));
        Reviews actual = reviewRepository.findByPostId(post.getId()).get();

        //then
        assertAll(() -> {
            assertThat(actual.getId()).isEqualTo(expected.getId());
            assertThat(actual.getWrite()).isEqualTo(expected.getWrite());
        });
    }

    @Test
    @DisplayName("게시물 아이디로 리뷰 조회시 존재하지 않으면 오류를 던진다.")
    void 게시물_아이디로_리뷰_조회시_존재하지안흥면_오류를_던진다(){
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when & then
        assertThatThrownBy(() -> reviewRepository.getByPostId(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("해당 검색결과로 존재햐는 글이 없습니다.");
    }
}