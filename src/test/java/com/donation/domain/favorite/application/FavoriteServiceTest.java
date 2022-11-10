package com.donation.domain.favorite.application;

import com.donation.domain.favorite.application.FavoriteService;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.favorite.repository.FavoriteRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
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
    @DisplayName("게시물 ID를 통해 게시물에 좋아요를 클릭한 유저 조회")
    void 게시물ID를_통해_게시물에_좋아요를_클릭한_유저_조회() {
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
    @DisplayName("게시물 ID를 통해 게시물에 저장된 모든 좋아요 정보 삭제")
    void 게시물_ID를_통해_게시물에_저장된_모든_좋아요_정보_삭제() {
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