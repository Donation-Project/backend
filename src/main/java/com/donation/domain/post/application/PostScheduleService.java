package com.donation.domain.post.application;

import com.donation.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.donation.domain.post.entity.PostState.DELETE;

@Component
@RequiredArgsConstructor
public class PostScheduleService {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void postStateIsDeleteAnd7DaysOver() {
        LocalDateTime localDateTime = LocalDateTime.now()
                .minus(7, ChronoUnit.DAYS);

        postRepository.deleteAll(postRepository.findAllByUpdateAtLessThanEqualAndState(localDateTime, DELETE));
    }
}
