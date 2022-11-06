package com.donation.controller.auth;

import com.donation.common.request.auth.TokenRenewalRequest;
import com.donation.common.utils.ControllerTest;
import com.donation.exception.DonationInvalidateException;
import com.donation.service.auth.application.AuthService;
import com.donation.web.controller.auth.AuthController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

import static com.donation.common.AuthFixtures.리프레쉬_토큰_발급_DTO;
import static com.donation.common.AuthFixtures.리프레쉬_토큰_응답_DTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerTest {

    @MockBean
    private AuthService authService;


    @Test
    @DisplayName("리프레쉬 토큰을 통해 새로운 엑세트 토큰을 발급한다.")
    void 리프레쉬_토큰을_통해_새로운_엑세스_토큰을_발급한다() throws Exception{
        //given
        given(authService.renewalToken(리프레쉬_토큰_발급_DTO())).willReturn(리프레쉬_토큰_응답_DTO());

        //expected
        mockMvc.perform(post("/api/auth/token/access")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(리프레쉬_토큰_발급_DTO())))
                .andExpect(status().isOk())
                .andDo(document("auth-refreshToken",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("리프레쉬 토큰이 유효하지 않을 경우 오류를 던진다.")
    void 리프레시_토큰이_유효하지_않을_경우_오류를_던진다() throws Exception {
        //given
        TokenRenewalRequest 잘못된_토큰_정보 = new TokenRenewalRequest("잘못된 토큰 정보");
        given(authService.renewalToken(any())).willThrow(new DonationInvalidateException("권한이 없습니다."));

        //expected
        mockMvc.perform(post("/api/auth/token/access")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(잘못된_토큰_정보)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("auth-fail-refreshToken",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("잘못된 토큰 정보")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("성공시 데이터 반환"),
                                fieldWithPath("error.errorCode").description("BAD_REQUEST"),
                                fieldWithPath("error.errorMessage").description("권한이 없습니다.")
                        )
                ));
    }
}
