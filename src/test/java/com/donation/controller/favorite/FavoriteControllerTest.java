package com.donation.controller.favorite;


import com.donation.common.UserFixtures;
import com.donation.common.response.user.UserRespDto;
import com.donation.common.utils.ControllerTest;
import com.donation.service.favorite.FavoriteService;
import com.donation.web.controller.favortie.FavoriteController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.FavoriteFixtures.좋아요_DTO;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
public class FavoriteControllerTest extends ControllerTest {
    @MockBean
    private FavoriteService favoriteService;

    @Test
    @DisplayName("좋아요 요청을 통한 저장")
    void 좋아요_요청을_통한_저장() throws Exception {
        //given
        willDoNothing().given(favoriteService).save(좋아요_DTO(1L, 1L));

        // expected
        mockMvc.perform(post("/api/favorite?type=SAVE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(좋아요_DTO(1L, 1L)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("favorite-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("type").description("저장 또는 취소")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요 취소 요청 성공")
    void 좋아요_취소_요청_성공() throws Exception {
        //given
        willDoNothing().given(favoriteService).cancel(좋아요_DTO(1L, 1L));


        // expected
        mockMvc.perform(post("/api/favorite?type=CANCEL")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(좋아요_DTO(1L, 1L)))
                )                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("favorite-cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("type").description("저장 또는 취소")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요 요청시 잘못된 정보를 통한 예외 발생")
    void 좋아요_요청시_잘못된_정보를_통한_예외_발생() throws Exception{
        //given
        String 잘못된값 = "잘못된 값";

        // expected
        mockMvc.perform(post("/api/favorite?type=CANCEL")
                        .param("type", 잘못된값)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(좋아요_DTO(1L, 1L)))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error.errorMessage").value("잘못된 정보입니다."));
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 전체 조회")
    void list() throws Exception{
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
