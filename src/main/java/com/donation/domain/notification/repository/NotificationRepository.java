package com.donation.domain.notification.repository;

import com.donation.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByMemberIdOrderByIdDesc(Long memberId);

    List<Notification> findAllByMemberIdAndDetectIsFalseOrderByIdDesc(Long memberId);

    @Query(value = "UPDATE Notification n SET n.isDetect = true WHERE n.id IN :ids")
    void changeDetectIsTrueByIdIn(List<Long> ids);

    void deleteAllByCreateAtLessThanAndDetectIsFalse(LocalDateTime createAt);

    boolean existsByMemberIdAndDetectIsFalse(Long memberId);
}
