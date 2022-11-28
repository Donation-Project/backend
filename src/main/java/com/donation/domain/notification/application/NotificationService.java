package com.donation.domain.notification.application;

import com.donation.domain.comment.repository.CommentRepository;
import com.donation.domain.notification.dto.NotificationResponse;
import com.donation.domain.notification.entity.Notification;
import com.donation.domain.notification.repository.NotificationRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.global.exception.DonationInvalidateException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.donation.domain.notification.entity.NotifyType.POST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private NotificationRepository notificationRepository;
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public List<NotificationResponse> findAll(Long memberId){
        List<Notification> notifications = notificationRepository.findAllByMemberIdOrderByIdDesc(memberId);
        return getByNotificationResponse(notifications);
    }

    public List<NotificationResponse> findAllByUncheckedNotification(Long memberId){
        List<Notification> notifications = notificationRepository.findAllByMemberIdAndDetectIsFalseOrderByIdDesc(memberId);
        return getByNotificationResponse(notifications);
    }

    public void checkedToNotification(Long memberId){
        validateExistByDetectIsFalse(memberId);
        List<Notification> notifications = notificationRepository.findAllByMemberIdAndDetectIsFalseOrderByIdDesc(memberId);
        List<Long> ids = getByIds(notifications);
        notificationRepository.changeDetectIsTrueByIdIn(ids);
    }

    private void validateExistByDetectIsFalse(Long memberId){
        if (!notificationRepository.existsByMemberIdAndDetectIsFalse(memberId))
            throw new DonationInvalidateException("읽지않은 알람이 존재하지 않습니다.");
    }

    private List<Long> getByIds(List<Notification> notifications){
        return notifications.stream()
                .map(Notification::getId)
                .collect(Collectors.toList());
    }

    private List<NotificationResponse> getByNotificationResponse(List<Notification> notifications){
        return notifications.stream()
                .filter(this::validateExistByPostEntityOrContentId)
                .map(NotificationResponse::of)
                .collect(Collectors.toList());
    }

    private boolean validateExistByPostEntityOrContentId(Notification notification){
        if (notification.getType().equals(POST))
            return postRepository.existsById(notification.getPostId());
        else //getType.equals(COMMENT)
            return commentRepository.existsById(notification.getCommentId());
    }


    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOrderThanDayNotification(){
        LocalDateTime day = LocalDateTime.now().minusMonths(6L);
        notificationRepository.deleteAllByCreateAtLessThanAndDetectIsFalse(day);
    }
}
