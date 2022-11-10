package com.donation.domain.post.repository;

import com.donation.common.PostFixtures;
import com.donation.common.utils.RepositoryTest;
import com.donation.domain.post.repository.PostRepository;
import com.donation.global.exception.DonationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PostRepositoryTest extends RepositoryTest {

    @Autowired
    private PostRepository postRepository;


    @Test
    @DisplayName("게시물번호로 게시물을 조회한다.")
    void 게시물번호로_게시물을_조회한다(){
        //given
        Long id = postRepository.save(PostFixtures.createPost()).getId();

        //when & then
        assertThat(postRepository.getById(id).getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 번호로 조회시 예외를 던진다")
    void 존재하지_않는_게시물번호로_조회시_예외를_던진다(){
        //given
        Long id = 0L;

        assertThatThrownBy(() -> postRepository.validateExistsById(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 게시글입니다.");
    }

    @Test
    @DisplayName("게시물 번호를 통해 데이터 베이스 락으로 조회")
    void 게시물번호를_통해_OptimisticLock메서드로_게시물_조회(){
        Long id = postRepository.save(PostFixtures.createPost()).getId();
        //when & then
        assertThat(postRepository.findByIdWithLock(id).getId()).isEqualTo(id);
    }
}
