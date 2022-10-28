package com.donation.repository.favorite;

import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.LongStream;

import static com.donation.common.TestEntityDataFactory.createPost;
import static com.donation.common.TestEntityDataFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRedisRepository favoriteRedisRepository;


    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void clear(){
        favoriteRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("좋아요(레퍼지토리) : 저장")
    void save(){
        //given
        Long userId = userRepository.save(createUser()).getId();
        Long postId = postRepository.save(createPost()).getId();

        //when
        favoriteRedisRepository.save(postId, userId);

        favoriteRedisRepository.getSetOperations().getOperations().exec();

        //then
        Boolean isPresent = favoriteRedisRepository.findById(postId, userId);
        assertThat(isPresent).isTrue();

        //clear
        favoriteRedisRepository.deleteAll(postId);
    }

    @Test
    @DisplayName("좋아요(레퍼지토리) : 전체 조회")
    void get(){
        //given
        Long postId = 1L;
        LongStream.range(0, 30L)
                .forEach(i -> favoriteRedisRepository.save(postId, i));

        favoriteRedisRepository.getSetOperations().getOperations().exec();

        //when
        List<Long> findAll = favoriteRedisRepository.findAll(postId);

        //then
        assertThat(findAll.size()).isEqualTo(30);

        //clear
        favoriteRedisRepository.deleteAll(postId);
    }

    @Test
    @DisplayName("좋아요(레퍼지토리) : count")
    void count(){
        //given
        Long postId = 1L;

        //when
        LongStream.range(0, 30L)
                .forEach(i -> favoriteRedisRepository.save(postId, i));

        favoriteRedisRepository.getSetOperations().getOperations().exec();

        //then
        Long count = favoriteRedisRepository.count(postId);
        assertThat(count).isEqualTo(30);

        //clear
        favoriteRedisRepository.deleteAll(postId);
    }
}