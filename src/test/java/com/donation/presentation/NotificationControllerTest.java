package com.donation.presentation;

import com.donation.common.utils.ControllerTest;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.notification.application.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.NotificationFixture.전체알림조회;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = NotificationController.class)
public class NotificationControllerTest extends ControllerTest {

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("사용자의 알림 전체 조회")
    void 사용자의_알림_전체_조회() throws Exception {
        //given
        Long userId = 1L;
        given(notificationService.findAll(userId)).willReturn(전체알림조회());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);


        //expected
        mockMvc.perform(get("/api/notification")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("notification-findAll",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        )
                ));
    }


    @Test
    @DisplayName("확인하지않은 알림 전체 조회")
    void 확인하지않은_알림_전체_조회() throws Exception {
        //given
        Long userId = 1L;
        given(notificationService.findAllByUncheckedNotification(userId)).willReturn(전체알림조회());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);


        //expected
        mockMvc.perform(get("/api/notification/uncheck")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("notification-findAllByUncheck",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("알림 읽은 표시로 변경")
    void 알림_읽은_표시로_변경() throws Exception{
        //given
        Long userId = 1L;
        BDDMockito.willDoNothing().given(notificationService).checkedToNotification(userId);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);


        //expected
        mockMvc.perform(put("/api/notification")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("notification-check",
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        )
                ));
    }
}
