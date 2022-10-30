package com.donation.repository.favorite;

import com.donation.common.FavoriteFixtures;
import com.donation.common.UserFixtures;
import com.donation.common.utils.RepositoryTest;
import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.exception.DonationDuplicateException;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.donation.common.FavoriteFixtures.createFavorites;
import static com.donation.common.TestEntityDataFactory.createPost;
import static com.donation.common.TestEntityDataFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteRepositoryTest extends RepositoryTest {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("좋아요 저장 요청 성공")
    void 좋아요_저장_요청_성공(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost());

        //when
        Favorites actual = favoriteRepository.save(FavoriteFixtures.createFavorites(user, post));

        //then
        assertThat(favoriteRepository.existsById(actual.getId())).isTrue();
    }

    @Test
    @DisplayName("이미 좋아요 버튼을 눌렀으면 예외를 던진다.")
    void 이미_좋아요_버튼을_눌렀으면_예외를_던진다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost());

        //when
        favoriteRepository.save(FavoriteFixtures.createFavorites(user, post));

        //then
        assertThatThrownBy(() -> favoriteRepository.validateExistsByPostIdAndUserId(post.getId(), user.getId()))
                .isInstanceOf(DonationDuplicateException.class)
                .hasMessage("이미 좋아요를 누른 회원입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 좋아요 정보로 조회시 예외를 던진다.")
    void 존재하지않는_좋아요_정보로_조회시_예외를_던진다(){
        //given
        Long userId = 0L;
        Long postId = 0L;

        //when & then
        assertThatThrownBy(() -> favoriteRepository.getByPostIdAndUserId(postId, userId))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 정보입니다.");
    }

    @Test
    @DisplayName("게시물번호로 저장된 좋아요 정보를 모두 삭제한다.")
    void 게시물번호로_저장된_좋아요_정보를_모두_삭제한다(){
        //given
        List<User> users = userRepository.saveAll(UserFixtures.creatUserList(1, 11));
        Post post = postRepository.save(createPost(users.get(0)));
        users.forEach(u -> favoriteRepository.save(createFavorites(u, post)));

        //when
        favoriteRepository.deleteAllByPostId(post.getId());

        //then
        assertThat(favoriteRepository.count()).isEqualTo(0);
    }
}