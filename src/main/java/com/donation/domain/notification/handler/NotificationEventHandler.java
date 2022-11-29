package com.donation.domain.notification.handler;

import com.donation.domain.comment.event.NewCommentEvent;
import com.donation.domain.comment.event.NewReplyEvent;
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

    @TransactionalEventListener
    public void newPostHandleNotification(NewPostEvent newPostEvent) {
        Notification notification = Notification.newPostToNotification(
                newPostEvent.getUserId(),
                newPostEvent.getPostId());
        notificationRepository.save(notification);
    }
    @TransactionalEventListener
    public void newCommentHandleNotification(NewCommentEvent newCommentEvent) {
        Notification notification = Notification.commentToNotification(
                newCommentEvent.getToUserId(),
                newCommentEvent.getPostId(),
                newCommentEvent.getCommentId());
        notificationRepository.save(notification);
    }

    @TransactionalEventListener
    public void newReplyHandleNotification(NewReplyEvent newReplyEvent) {
        Notification notification = Notification.replyCommentToNotification(
                newReplyEvent.getToUserId(),
                newReplyEvent.getCommentId());
        notificationRepository.save(notification);
    }


    //TODO: 게시물에 기부시 해당 작성자에게 알림


    //TODO: 게시물 좋아요 요청시 게시물 작성자에게 알림
}
