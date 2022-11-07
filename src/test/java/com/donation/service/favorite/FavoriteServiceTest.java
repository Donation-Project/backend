package com.donation.service.favorite;

import com.donation.common.response.user.UserRespDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.favorite.FavoriteRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.creatUserList;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteServiceTest extends ServiceTest {
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("좋아요 요청 성공")
    void 좋아요_요청_성공() {
        //given
        Long userId = userRepository.save(createUser()).getId();
        Long postId = postRepository.save(createPost()).getId();

        //when
        favoriteService.save(회원검증(userId), postId);

        //then
        assertThat(favoriteRepository.existsByPostIdAndUserId(postId, userId)).isTrue();
    }

    @Test
    @DisplayName("좋아요 취소 요청 성공")
    void 좋아요_취소_요청_성공() {
        //given
        Long userId = userRepository.save(createUser()).getId();
        Long postId = postRepository.save(createPost()).getId();
        favoriteService.save(회원검증(userId), postId);

        //when
        favoriteService.cancel(회원검증(userId), postId);

        //then
        assertThat(favoriteRepository.existsByPostIdAndUserId(postId, userId)).isFalse();
    }


    @Test
    @DisplayName("좋아요(서비스) : 리스트 조회")
    void getList() {
        //given
        List<User> users = userRepository.saveAll(creatUserList(1, 11));
        Post post = postRepository.save(createPost(users.get(0)));
        users.forEach(u -> favoriteService.save(회원검증(u.getId()), post.getId()));

        //when
        List<UserRespDto> result = favoriteService.findAll(post.getId());

        //then
        assertAll(() -> {
            assertThat(result.size()).isEqualTo(10);
            assertThat(result.get(9).getId()).isEqualTo(users.get(9).getId());
            assertThat(result.get(9).getId()).isEqualTo(users.get(9).getId());
        });
    }


    @Test
    @DisplayName("좋아요(서비스) : 포스팅 전체 좋아요 삭제")
    void delete() {
        //given
        List<User> users = userRepository.saveAll(creatUserList(1, 31));
        Post post = postRepository.save(createPost(users.get(0)));
        users.forEach(u -> favoriteService.save(회원검증(u.getId()), post.getId()));

        //when
        favoriteService.deletePostId(post.getId());

        //then
        assertThat(favoriteRepository.count()).isEqualTo(0);
    }
}