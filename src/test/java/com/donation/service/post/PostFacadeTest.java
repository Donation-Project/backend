package com.donation.service.post;

import com.donation.common.PostFixtures;
import com.donation.common.UserFixtures;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostFacadeTest  {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("동시에 100명이 기부 요청을 하여 현재모인 금액이 증가한다")
    void 동시에_100명이_기부_요청을_하여_현재모인_금액이_증가한다() throws InterruptedException {
        //given
        User user = userRepository.save(UserFixtures.createUser());
        Long postId = postRepository.saveAndFlush(PostFixtures.createPost(user)).getId();

        //테스트 시간에 따른 5으로 수정
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    postFacade.increase(postId, 1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        Post post = postRepository.getById(postId);
        assertThat(post.getCurrentAmount()).isEqualTo(100);
    }
}
