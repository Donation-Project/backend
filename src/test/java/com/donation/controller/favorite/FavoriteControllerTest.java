package com.donation.controller.favorite;


import com.donation.common.UserFixtures;
import com.donation.common.response.user.UserRespDto;
import com.donation.common.utils.ControllerTest;
import com.donation.service.auth.application.AuthService;
import com.donation.service.favorite.FavoriteService;
import com.donation.web.controller.favortie.FavoriteController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.AuthFixtures.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FavoriteController.class)
public class FavoriteControllerTest extends ControllerTest {
    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("좋아요 요청을 통한 저장")
    void 좋아요_요청을_통한_저장() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(favoriteService).save(회원검증(id), 1L);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);
        // expected
        mockMvc.perform(post("/api/favorite/{id}?type=SAVE",1L)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print())
                .andDo(document("favorite-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        requestParameters(
                                parameterWithName("type").description("저장 또는 취소")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요 취소 요청 성공")
    void 좋아요_취소_요청_성공() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(favoriteService).cancel(회원검증(id), 1L);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);


        // expected
        mockMvc.perform(post("/api/favorite/{id}?type=CANCEL",1L)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print())
                .andDo(document("favorite-cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        requestParameters(
                                parameterWithName("type").description("저장 또는 취소")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요 요청시 잘못된 정보를 통한 예외 발생")
    void 좋아요_요청시_잘못된_정보를_통한_예외_발생() throws Exception {
        //given
        String 잘못된값 = "잘못된 값";

        // expected
        mockMvc.perform(post("/api/favorite/{id}", 1L)
                        .param("type", 잘못된값)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error.errorMessage").value("잘못된 정보입니다."));
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 전체 조회")
    void list() throws Exception {
        //given
        List<UserRespDto> response = LongStream.range(1, 11)
                .mapToObj(UserFixtures::일반_반환_데이터)
                .collect(Collectors.toList());
        given(favoriteService.findAll(1L)).willReturn(response);

        // expected
        mockMvc.perform(get("/api/favorite")
                        .param("postId", "1")
                ).andExpect(status().isOk())
                .andDo(document("favorite-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("postId").description("포스팅 ID")
                        )
                ));
    }
}
