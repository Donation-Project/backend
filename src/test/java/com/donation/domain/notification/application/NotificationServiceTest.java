package com.donation.domain.notification.application;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.notification.dto.NotificationResponse;
import com.donation.domain.notification.repository.NotificationRepository;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static com.donation.domain.notification.entity.Notification.newPostToNotification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class NotificationServiceTest extends ServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;


    @Test
    @DisplayName("사용자의 알림 전체 조회")
    void 사용자의_알림_전체_조회(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        notificationRepository.saveAll(LongStream.range(0, 5)
                .mapToObj(i -> newPostToNotification(user.getId(), post.getId()))
                .collect(Collectors.toList()));

        //when
        List<NotificationResponse> actual = notificationService.findAll(user.getId());

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(5);
            assertThat(actual.get(0).getPostId()).isEqualTo(post.getId());
        });
    }

    @Test
    @DisplayName("확인하지 않은 알람 전체 조회")
    void 확인하지_않은_알람_전체_조회(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        notificationRepository.saveAll(LongStream.range(0, 5)
                .mapToObj(i -> newPostToNotification(user.getId(), post.getId()))
                .collect(Collectors.toList()));

        //when
        List<NotificationResponse> actual = notificationService.findAllByUncheckedNotification(user.getId());

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(5);
            assertThat(actual.get(0).getPostId()).isEqualTo(post.getId());
        });
    }

    @Test
    @DisplayName("읽음 표시를 할 알림이 없을 경우 오류를 던진다.")
    void 읽음_표시를_할_알림이_없을_경우_오류를_던진다(){
        //given
        Long memberId = 1L;

        //when & then
        assertThatThrownBy(() -> notificationService.checkedToNotification(memberId))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("읽지않은 알람이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("읽지 않은 알림에 경우 읽음 표시를 남긴다.")
    void 읽지_않은_알림에_경우_읽음_표시를_남긴다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        notificationRepository.saveAll(LongStream.range(0, 5)
                .mapToObj(i -> newPostToNotification(user.getId(), post.getId()))
                .collect(Collectors.toList()));

        //when
        notificationService.checkedToNotification(user.getId());

        //then
        assertThat(notificationRepository.existsByMemberIdAndConformIsFalse(user.getId())).isFalse();
    }
}
