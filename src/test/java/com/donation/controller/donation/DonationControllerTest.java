package com.donation.controller.donation;


import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;

import com.donation.common.utils.ControllerTest;

import com.donation.domain.entites.User;

import com.donation.service.auth.application.AuthService;
import com.donation.service.donation.DonationService;

import com.donation.web.controller.donation.DonationController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;


import java.util.List;
import java.util.stream.Collectors;

import java.util.stream.LongStream;

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.DonationFixtures.*;

import static com.donation.common.PostFixtures.*;

import static com.donation.common.UserFixtures.createUser;

import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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
                .mapToObj(i -> 유저에_대한_기부_응답(createPost(i) ,user))
                .collect(Collectors.toList());

        given(authService.extractMemberId(엑세스_토큰)).willReturn(user.getId());
        given(donationService.findById(회원검증(user.getId()))).willReturn(content);

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
                                fieldWithPath("data.[].title").description("제목"),
                                fieldWithPath("data.[].amount").description("금액"),
                                fieldWithPath("data.[].currentAmount").description("포스트 총 후원금액"),
                                fieldWithPath("data.[].postId").description("포스팅 ID"),
                                fieldWithPath("data.[].userId").description("유저 ID"),
                                fieldWithPath("data.[].category").description("카테고리"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }


}