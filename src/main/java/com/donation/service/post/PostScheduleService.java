package com.donation.service.post;

import com.donation.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.donation.domain.enums.PostState.DELETE;

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
