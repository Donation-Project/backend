package com.donation.domain.notification.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static com.donation.domain.notification.entity.NotifyType.*;
import static javax.persistence.EnumType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "notification")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;
    @Column(nullable = false)
    private Long userId;
    private Long postId;
    private Long commentId;
    @Enumerated(STRING)
    private NotifyType type;

    private boolean conform;

    @Builder
    public Notification(Long userId, Long postId, Long commentId, NotifyType type) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.type = type;
    }


    public static Notification newPostToNotification(Long userId, Long postId){
        return new Notification(userId, postId, null, POST);
    }

    public static Notification donateToNotification(Long userId, Long postId){
        return new Notification(userId, postId, null, DONATE);
    }

    public static Notification commentToNotification(Long userId, Long postId, Long commentId){
        return new Notification(userId, postId, commentId, COMMENT);
    }

    public static Notification replyCommentToNotification(Long userId, Long postId, Long commentId){
        return new Notification(userId, postId, commentId, REPLY);
    }

    public static Notification likeToNotification(Long userId, Long postId){
        return new Notification(userId, postId, null, LIKE);
    }

    public void validateOwner(Long userId){
        if (!Objects.equals(this.userId, userId))
            throw new DonationInvalidateException("잘못된 알람 요청입니다.");
    }
}
