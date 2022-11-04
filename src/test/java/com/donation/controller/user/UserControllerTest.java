package com.donation.controller.user;

import com.donation.common.UserFixtures;
import com.donation.common.response.user.UserRespDto;
import com.donation.common.utils.ControllerTest;
import com.donation.repository.utils.PageCustom;
import com.donation.service.auth.application.AuthService;
import com.donation.service.user.UserService;
import com.donation.web.controller.user.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.UserFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest extends ControllerTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("회원가입 요청 성공")
    void 회원가입_요청_성공() throws Exception {
        //given
        Long id = authService.save(유저_회원가입_DTO);
        given(authService.save(유저_회원가입_DTO)).willReturn(id);

        // expected
        mockMvc.perform(post("/api/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유저_회원가입_DTO))

                )
                .andExpect(status().isCreated())
                .andDo(document("user-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

//    @Test
//    @DisplayName("로그인 요청 성공")
//    void 로그인_요청_성공() throws Exception {
//        //given
//        given(authService.login(유저_로그인_DTO)).willReturn();
//
//        // expected
//        mockMvc.perform(post("/api/login")
//                        .contentType(APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(유저_로그인_DTO))
//                )
//                .andExpect(status().isOk())
//                .andDo(document("user-login",
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        responseFields(
//                                fieldWithPath("success").description("성공 여부"),
//                                fieldWithPath("data").description("유저 ID"),
//                                fieldWithPath("error").description("에러 발생시 오류 반환")
//                        )
//                ));
//    }

    @Test
    @DisplayName("회원의 ID를 통한 회원단건조회 요청 성공")
    void 회원의_ID를_통한_회원단건조회_요청_성공() throws Exception {
        //given
        Long id = 1L;
        given(userService.findById(id)).willReturn(일반_반환_데이터(id));

        // expected
        mockMvc.perform(get("/api/user/{id}", id))
                .andExpect(status().isOk())
                .andDo(document("user-inquiry",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.id").description("유저 ID"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.name").description("이름"),
                                fieldWithPath("data.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.metamask").description("메타마스크 주소"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("회원 리스트 조회시 10개씩 페이징되어 조회된다.")
    void 회원_리스트_조회시_10개씩_페이징되어_조회된다() throws Exception{
        //given
        List<UserRespDto> contet = LongStream.range(1, 11).mapToObj(UserFixtures::일반_반환_데이터).collect(Collectors.toList());
        PageCustom<UserRespDto> response = new PageCustom<>(PageableExecutionUtils.getPage(contet, PageRequest.of(0, 10), () -> 20L));
        given(userService.getList(any())).willReturn(response);

        // expected
        mockMvc.perform(get("/api/user?page=0&size=10"))
                .andExpect(status().isOk())
                .andDo(document("user-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("회원 프로필 수정 요청 성공")
    void 회원_프로필_수정_요청_성공() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(userService).updateProfile(id, 유저_프로필_업데이트_DTO);

        //expected
        mockMvc.perform(put("/api/user/{id}/profile", id)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유저_프로필_업데이트_DTO)))
                .andExpect(status().isOk())
                .andDo(document("user-profileImage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}