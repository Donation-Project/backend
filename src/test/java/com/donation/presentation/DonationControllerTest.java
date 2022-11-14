package com.donation.presentation;


import com.donation.common.utils.ControllerTest;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.service.DonationService;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.DonationFixtures.*;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DonationController.class)
class DonationControllerTest extends ControllerTest {
    @MockBean
    private DonationService donationService;
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("유저_아이디로_후원내역을_조회한다")
    void 유저_아이디로_후원내역을_조회한다() throws Exception {
        //given
        User user = createUser(1L);
        List<DonationFindRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 유저에_대한_기부_응답(i, createPost(i) ,user))
                .collect(Collectors.toList());

        given(authService.extractMemberId(엑세스_토큰)).willReturn(user.getId());
        given(donationService.findMyDonation(회원검증(user.getId()))).willReturn(content);
        // expected
        mockMvc.perform(get("/api/donation/me")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("donation-findById",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.[].donationId").description("후원 ID"),
                                fieldWithPath("data.[].title").description("제목"),
                                fieldWithPath("data.[].amount").description("금액"),
                                fieldWithPath("data.[].grossAmount").description("포스트 총 후원금액"),
                                fieldWithPath("data.[].postId").description("포스팅 ID"),
                                fieldWithPath("data.[].userId").description("유저 ID"),
                                fieldWithPath("data.[].category").description("카테고리"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("모든_후원내역을_조회한다")
    void 모든_후원내역을_조회한다() throws Exception {
        //given
        User user = createUser(1L);
        Post post = createPost(createUser(2L));
        List<DonationFindByFilterRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 기부_전체조회_응답(i, post, user))
                .collect(Collectors.toList());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(user.getId());
        given(donationService.findAllDonationByFilter(eq(회원검증(user.getId())), any())).willReturn(content);
        // expected
        mockMvc.perform(get("/api/donation")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(기부_전체검색_DTO())))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("donation-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.[].donateId").description("후원 ID"),
                                fieldWithPath("data.[].postId").description("포스팅 ID"),
                                fieldWithPath("data.[].userId").description("유저 ID"),
                                fieldWithPath("data.[].title").description("제목"),
                                fieldWithPath("data.[].amount").description("후원량"),
                                fieldWithPath("data.[].grossAmount").description("포스트 총 후원금액"),
                                fieldWithPath("data.[].fromUser").description("후원자"),
                                fieldWithPath("data.[].toUser").description("후원 받는사람"),
                                fieldWithPath("data.[].category").description("카테고리"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("모든_후원내역을_조회할때_관리자가_아닐경우_예외를_던진다")
    void 모든_후원내역을_조회할때_관리자가_아닐경우_예외를_던진다() throws Exception {
        //given
        User user = createUser(1L);
        Post post = createPost(createUser(2L));
        List<DonationFindByFilterRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 기부_전체조회_응답(i, post, user))
                .collect(Collectors.toList());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(user.getId());
        given(donationService.findAllDonationByFilter(eq(회원검증(user.getId())), any())).willThrow(new DonationInvalidateException("권한이_없습니다"));
        //expected
        mockMvc.perform(get("/api/donation")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(기부_전체검색_DTO())))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("donation-getList-exception",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("실패시 반환하지 않는다"),
                                fieldWithPath("error.errorCode").description("에러코드"),
                                fieldWithPath("error.errorMessage").description("에러 메시지")
                        )
                ));

    }

}