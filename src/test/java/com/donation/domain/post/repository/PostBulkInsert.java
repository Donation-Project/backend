package com.donation.domain.post.repository;


import com.donation.common.PostFixtures;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsert {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostJdbcRepository postJdbcRepository;

    @Test
    @DisplayName("게시물 랜덤 데이터 벌크 Insert")
    void 게시물_랜덤_데이터_벌크_Insert(){
        //given
        int insertNum = 0;
        User user = userRepository.getById(15L);
        EasyRandom easyRandom = PostFixtures.createRandomPost(user);

        var stopWatch = new StopWatch();
        stopWatch.start();

        List<Post> posts = IntStream.range(0, insertNum)
                .parallel()
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .collect(Collectors.toList());

        stopWatch.stop();
        System.out.println(insertNum + "개 데이터 생성 시간 = " + stopWatch.getTotalTimeSeconds());


        var insertWatch = new StopWatch();
        insertWatch.start();

        postJdbcRepository.bulkInsert(posts);

        insertWatch.stop();
        System.out.println(insertNum + "개 데이터 저장 시간= " + insertWatch.getTotalTimeSeconds());
    }

}
