package com.donation.presentation;

import com.donation.common.utils.ControllerTest;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.reviews.application.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.ReviewFixture.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest extends ControllerTest {

    @MockBean
    private ReviewService reviewService;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("감사글 저장 요청을 성공한다.")
    void 감사글_저장_요청을_성공한다() throws Exception {
        //given
        Long userId = 1L;
        Long postId = 1L;
        given(reviewService.save(회원검증(userId), postId, 감사글())).willReturn(1L);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);

        //expected
        mockMvc.perform(post("/api/post/{id}/reviews", postId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(감사글()))
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("review-create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        )
                ));
    }

    @Test
    @DisplayName("감사글 수정 요청을 성공한다.")
    void 감사글_수정_요청을_성공한다() throws Exception{
        Long userId = 1L;
        Long postId = 1L;
        willDoNothing().given(reviewService).changeContent(회원검증(userId), postId, 수정된_감사글());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);

        //expected
        mockMvc.perform(put("/api/post/{id}/reviews", postId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(수정된_감사글()))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("review-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        )
                ));
    }



    @Test
    @DisplayName("게시물 아이디를 통한 회원 단건조회 요청 성공")
    void 게시물_아이디를_통한_회원_단건조회_요청_성공() throws Exception{
        //given
        Long postId = 1L;
        given(reviewService.getReview(postId)).willReturn(감사글_조회_결과());

        //expected
        mockMvc.perform(get("/api/post/{id}/reviews", postId))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("review-get",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.reviewId").description("감사글 ID"),
                                fieldWithPath("data.write.title").description("감사글 제목"),
                                fieldWithPath("data.write.content").description("감사글 내용"),
                                fieldWithPath("data.userRespDto.id").description("유저 ID"),
                                fieldWithPath("data.userRespDto.email").description("유저 이메일"),
                                fieldWithPath("data.userRespDto.name").description("유저 이름"),
                                fieldWithPath("data.userRespDto.profileImage").description("유저 프로필 이미지 URL"),
                                fieldWithPath("data.userRespDto.metamask").description("유저 메타마스크 주소"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("감사글을 삭제요청을 성공한다.")
    void 감사글_삭제요청을_성공한다() throws Exception{
        Long userId = 1L;
        Long postId = 1L;
        willDoNothing().given(reviewService).delete(회원검증(userId), postId);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);


        mockMvc.perform(delete("/api/post/{id}/reviews", postId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("review-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        )
                ));
    }
}
