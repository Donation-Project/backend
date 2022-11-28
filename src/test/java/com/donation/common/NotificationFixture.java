package com.donation.common;

import com.donation.domain.notification.dto.NotificationResponse;
import com.donation.domain.notification.entity.Notification;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NotificationFixture {

    public static Notification 포스트알림(){
        return Notification.newPostToNotification(1L, 1L);
    }

    public static List<NotificationResponse> 전체알림조회(){
        return IntStream.range(0, 5)
                .mapToObj(i -> 포스트알림())
                .collect(Collectors.toList())
                .stream().map(NotificationResponse::of)
                .collect(Collectors.toList());
    }
}
