package com.donation.presentation;


import com.donation.common.utils.ControllerTest;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.service.DonationService;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
                                fieldWithPath("data.[].donationId").description("아이디"),
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
        User user = createUser(2L);
        Post post = createPost(createUser(1L));
        List<DonationFindByFilterRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 기부_전체조회_응답(i,post ,user))
                .collect(Collectors.toList());

        given(donationService.findAllDonationByFilter(기부_전체검색_DTO())).willReturn(content);

        // expected
        mockMvc.perform(get("/api/donation")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(기부_전체검색_DTO())))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("donation-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("유저이름"),
                                fieldWithPath("category").description("포스트 카테고리")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.[].donateId").description("후원 ID"),
                                fieldWithPath("data.[].postId").description("포스팅 ID"),
                                fieldWithPath("data.[].userId").description("유저 ID"),
                                fieldWithPath("data.[].fromUser").description("후원자 이름"),
                                fieldWithPath("data.[].toUser").description("후원대상자 이름"),
                                fieldWithPath("data.[].title").description("제목"),
                                fieldWithPath("data.[].amount").description("후원량"),
                                fieldWithPath("data.[].grossAmount").description("포스트 총 후원금액"),
                                fieldWithPath("data.[].category").description("카테고리"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

}