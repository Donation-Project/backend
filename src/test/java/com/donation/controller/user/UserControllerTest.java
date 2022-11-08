package com.donation.controller.user;

import com.donation.auth.LoginMember;
import com.donation.common.UserFixtures;
import com.donation.common.request.user.UserEmailReqDto;
import com.donation.common.response.user.UserEmailRespDto;
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

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.UserFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    @DisplayName("로그인 요청 성공")
    void 로그인_요청_성공() throws Exception {
        //given
        given(authService.login(any())).willReturn(로그인_응답_DTO());

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유저_로그인_DTO))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.accessToken").description("엑세스 토큰"),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 중복확인 요청 성공(중복되지 않은 이메일)")
    void 이메일_중복확인_요청_성공() throws Exception {
        //given
        given(userService.checkUniqueEmail(일반_사용자_이메일)).willReturn(UserEmailRespDto.of(false));

        //expected
        mockMvc.perform(post("/api/join/exists")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserEmailReqDto(일반_사용자_이메일)))
                )
                .andDo(print())
                .andDo(document("user-email",
                        preprocessRequest(),
                        preprocessResponse(),
                        requestFields(
                                fieldWithPath("email").description("중복 확인 요청 이메일")
                        ))
                );
    }

    @Test
    @DisplayName("회원의 ID를 통한 회원단건조회 요청 성공")
    void 회원의_ID를_통한_회원단건조회_요청_성공() throws Exception {
        //given
        Long id = 1L;
        given(userService.findById(any())).willReturn(일반_반환_데이터(id));
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);

        // expected
        mockMvc.perform(get("/api/user/me")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-me",
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
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
    void 회원_리스트_조회시_10개씩_페이징되어_조회된다() throws Exception {
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
        LoginMember 로그인정보 = new LoginMember(id);
        willDoNothing().given(userService).updateProfile(로그인정보, 유저_프로필_업데이트_DTO);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);

        //expected
        mockMvc.perform(put("/api/user/profile")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(유저_프로필_업데이트_DTO)))
                .andExpect(status().isOk())
                .andDo(document("user-profileImage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        requestFields(
                                fieldWithPath("profileImage").description("Base64 인코딩 이미지")
                        )

                ));
    }
}