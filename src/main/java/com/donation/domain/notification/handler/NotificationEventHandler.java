package com.donation.domain.notification.handler;

import com.donation.domain.comment.event.NewCommentNotificationEvent;
import com.donation.domain.comment.event.NewReplyNotificationEvent;
import com.donation.domain.donation.event.DonateNotificationEvent;
import com.donation.domain.notification.entity.Notification;
import com.donation.domain.notification.repository.NotificationRepository;
import com.donation.domain.post.event.NewPostNotificationEvent;
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

    @TransactionalEventListener
    public void newPostHandleNotification(NewPostNotificationEvent newPostNotificationEvent) {
        Notification notification = Notification.newPostToNotification(
                newPostNotificationEvent.getUserId(),
                newPostNotificationEvent.getPostId());
        notificationRepository.save(notification);
    }
    @TransactionalEventListener
    public void newCommentHandleNotification(NewCommentNotificationEvent newCommentNotificationEvent) {
        Notification notification = Notification.commentToNotification(
                newCommentNotificationEvent.getToUserId(),
                newCommentNotificationEvent.getPostId(),
                newCommentNotificationEvent.getCommentId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void newReplyHandleNotification(NewReplyNotificationEvent newReplyNotificationEvent) {
        Notification notification = Notification.replyCommentToNotification(
                newReplyNotificationEvent.getToUserId(),
                newReplyNotificationEvent.getPostId(),
                newReplyNotificationEvent.getCommentId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void donateHandleNotification(DonateNotificationEvent donateNotificationEvent) {
        Notification notification = Notification.donateToNotification(
                donateNotificationEvent.getUserId(),
                donateNotificationEvent.getPostId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void postLikeHandleNotification(){

    }
}
