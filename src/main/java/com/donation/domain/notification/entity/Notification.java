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
    private Long memberId;
    private Long postId;
    private Long commentId;
    @Enumerated(STRING)
    private NotifyType type;

    private boolean conform;

    @Builder
    public Notification(Long memberId, Long postId, Long commentId, NotifyType type) {
        this.memberId = memberId;
        this.postId = postId;
        this.commentId = commentId;
        this.type = type;
    }


    public static Notification newPostToNotification(Long memberId, Long postId){
        return new Notification(memberId, postId, null, POST);
    }

    public static Notification donateToNotification(Long memberId, Long postId){
        return new Notification(memberId, postId, null, DONATE);
    }

    public static Notification replyCommentToNotification(Long memberId, Long commentId){
        return new Notification(memberId, null, commentId, REPLY);
    }

    public static Notification likeToNotification(Long memberId, Long postId){
        return new Notification(memberId, postId, null, LIKE);
    }

    public void validateOwner(Long memberId){
        if (!Objects.equals(this.memberId, memberId))
            throw new DonationInvalidateException("잘못된 알람 요청입니다.");
    }
}
