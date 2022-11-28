package com.donation.domain.notification.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.Objects;

import static com.donation.domain.notification.entity.NotifyType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "Notification")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long toMemberId;
    private Long fromMemberId;
    private Long postId;
    private Long commentId;
    @Embedded
    private NotifyType notifyType;
    private boolean isDetect;

    @Builder
    public Notification(Long toMemberId, Long fromMemberId, Long postId, Long commentId, NotifyType notifyType) {
        this.toMemberId = toMemberId;
        this.fromMemberId = fromMemberId;
        this.postId = postId;
        this.commentId = commentId;
        this.notifyType = notifyType;
    }


    public static Notification newPostToNotification(Long toMemberId, Long postId){
        return new Notification(toMemberId, null, postId, null, POST);
    }

    public static Notification donateToNotification(Long toMemberId, Long fromMemberId, Long postId){
        return new Notification(toMemberId, fromMemberId, postId, null, DONATE);
    }

    public static Notification replyCommentToNotification(Long toMemberId, Long fromMemberId, Long commentId){
        return new Notification(toMemberId, fromMemberId, null, commentId, REPLY);
    }

    public static Notification likeToNotification(Long toMemberId, Long fromMemberId, Long postId){
        return new Notification(toMemberId, fromMemberId, postId, null, LIKE);
    }

    public void validateOwner(Long toMemberId){
        if (!Objects.equals(this.toMemberId, toMemberId))
            throw new DonationInvalidateException("잘못된 알람 요청입니다.");
    }

    public void changeToDetect(){
        this.isDetect = true;
    }
}
