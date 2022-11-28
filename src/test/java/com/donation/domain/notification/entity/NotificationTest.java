package com.donation.domain.notification.entity;

import com.donation.global.exception.DonationInvalidateException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.domain.notification.entity.NotifyType.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class NotificationTest {

    @Test
    @DisplayName("정상적으로 알림이 생성된다.")
    void 정상적으로_알림_생성된다() {
        //given
        Long memberId = 1L;
        Long postId = 1L;

        //when & then
        assertDoesNotThrow(() -> Notification.builder()
                .memberId(memberId)
                .postId(postId)
                .type(POST)
                .build());
    }

    @Test
    @DisplayName("알림 대상자가 아니면 오류를 던진다.")
    void 알림이_대상자가_아니면_오류를_던진다() {
        //given
        Long memberId = 0L;

        //when
        Notification actual = Notification.builder().memberId(1L).build();

        //then
        Assertions.assertThatThrownBy(() -> actual.validateOwner(memberId))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("잘못된 알람 요청입니다.");
    }
}
