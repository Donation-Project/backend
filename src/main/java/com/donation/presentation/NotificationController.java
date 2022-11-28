package com.donation.presentation;

import com.donation.domain.notification.application.NotificationService;
import com.donation.domain.notification.dto.NotificationResponse;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<?> findByAll(@LoginInfo LoginMember loginMember){
        List<NotificationResponse> responses = notificationService.findAll(loginMember.getId());
        return ResponseEntity.ok(CommonResponse.success(responses));
    }

    @GetMapping("/uncheck")
    public ResponseEntity<?> findByAllUncheck(@LoginInfo LoginMember loginMember){
        List<NotificationResponse> responses = notificationService.findAllByUncheckedNotification(loginMember.getId());
        return ResponseEntity.ok(CommonResponse.success(responses));
    }

    @PutMapping
    public ResponseEntity<?> checkedToNotification(@LoginInfo LoginMember loginMember) {
        notificationService.checkedToNotification(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
