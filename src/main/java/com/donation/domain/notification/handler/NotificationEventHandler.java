package com.donation.domain.notification.handler;

import com.donation.domain.notification.entity.Notification;
import com.donation.domain.notification.repository.NotificationRepository;
import com.donation.domain.post.event.NewPostEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
@Async
@RequiredArgsConstructor
@Transactional(propagation = REQUIRES_NEW)
public class NotificationEventHandler {

    private final NotificationRepository notificationRepository;

    //TODO: 댓글 작성시 게시물 작성자에게 알림

    //TODO: 대댓글 작성시 해당 댓글 작성자에게 알림

    //TODO: 게시물에 기부시 해당 작성자에게 알림

    //TODO: 게시물 등록시 등록확인 알림
    @TransactionalEventListener
    public void newPostHandleNotification(NewPostEvent newPostEvent) {
        Notification notification = Notification.newPostToNotification(
                newPostEvent.getUserId(),
                newPostEvent.getPostId());
        notificationRepository.save(notification);
    }

    //TODO: 게시물 좋아요 요청시 게시물 작성자에게 알림
}
