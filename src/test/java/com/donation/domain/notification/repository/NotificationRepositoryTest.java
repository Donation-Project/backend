package com.donation.domain.notification.repository;

import com.donation.common.utils.RepositoryTest;
import com.donation.domain.notification.entity.Notification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.domain.notification.entity.Notification.newPostToNotification;

public class NotificationRepositoryTest extends RepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("회원아이디를 통해 확인하지 않은 알림이 있는지 확인한다.")
    void 회원아이디를_통해_확인하지않은_알림이_알림이_있는지_확인한다(){
        //given
        Long userId = 0L;
        Notification notification = newPostToNotification(userId, 1L);
        notificationRepository.save(notification);

        //when & then
        Assertions.assertThat(notificationRepository.existsByUserIdAndConformIsFalse(userId)).isTrue();
    }

    @Test
    @DisplayName("확인하지않은 알림을 모두 확인 상태로 변경한다.")
    void 확인하지않은_알림을_모두_확인_상태로_변경한다(){
        //given
        Long userId = 0L;
        List<Long> ids = notificationRepository.saveAll(LongStream.range(0, 5)
                        .mapToObj(i -> newPostToNotification(userId, i))
                        .collect(Collectors.toList()))
                .stream().map(Notification::getId)
                .collect(Collectors.toList());

        //when
        notificationRepository.changeDetectIsTrueByIdIn(ids);

        //then
        Assertions.assertThat(notificationRepository.existsByUserIdAndConformIsFalse(userId)).isFalse();
    }

    @Test
    @DisplayName("확인후 6개월이 지난 알림의 경우 삭제한다.")
    void 확인후_6개월이_지난_알림의_경우_삭제한다(){
        //given
        Long userId = 0L;
        Notification notification = newPostToNotification(userId, 1L);
        notificationRepository.save(notification);

        //when
        LocalDateTime request = LocalDateTime.now().minusMonths(6);
        notificationRepository.deleteAllByUpdateAtLessThanAndConformIsTrue(request);

        //when
        //알람을 확인하지않았기 때문에 삭제되지 않는다.
        Assertions.assertThat(notificationRepository.existsByUserIdAndConformIsFalse(userId)).isTrue();
    }

    @Test
    @DisplayName("확인하지 않은 알람을 조회한다.")
    void 확인하지않은_알람을_조회한다(){
        //given
        Long userId = 0L;
        notificationRepository.saveAll(LongStream.range(0, 5)
                        .mapToObj(i -> newPostToNotification(userId, i))
                        .collect(Collectors.toList()));

        //when
        List<Notification> actual = notificationRepository.findAllByUserIdAndConformIsFalseOrderByIdDesc(userId);

        //then
        Assertions.assertThat(actual.size()).isEqualTo(5);
    }

    @Test
    @DisplayName("사용자의 알람을 모두 조회한다.")
    void 회원의_알람을_모두_조회한다(){
        //given
        Long userId = 0L;
        notificationRepository.saveAll(LongStream.range(0, 5)
                .mapToObj(i -> newPostToNotification(userId, i))
                .collect(Collectors.toList()));

        //when
        List<Notification> actual = notificationRepository.findAllByUserIdOrderByIdDesc(userId);


        //then
        Assertions.assertThat(actual.size()).isEqualTo(5);
    }
}
