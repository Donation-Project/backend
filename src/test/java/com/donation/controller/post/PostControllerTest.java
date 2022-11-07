package com.donation.controller.post;

import com.donation.common.response.post.PostListRespDto;
import com.donation.common.utils.ControllerTest;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.utils.PageCustom;
import com.donation.service.auth.application.AuthService;
import com.donation.service.post.PostService;
import com.donation.web.controller.post.PostController;
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
import static com.donation.common.PostFixtures.*;
import static com.donation.common.UserFixtures.createUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
class PostControllerTest extends ControllerTest {
    @MockBean
    private PostService postService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("게시물 등록 요청 성공")
    void 게시물_등록_요청_성공() throws Exception {
        //given
        Long id = 1L;
        given(postService.createPost(게시물_생성_DTO(), 회원검증(id))).willReturn(게시물_생성_응답());
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);

        // expected
        mockMvc.perform(post("/api/post")
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(게시물_생성_DTO()))
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.postId").description("포스팅 ID"),
                                fieldWithPath("data.userRespDto.id").description("유저 아이디"),
                                fieldWithPath("data.userRespDto.email").description("이메일"),
                                fieldWithPath("data.userRespDto.name").description("이름"),
                                fieldWithPath("data.userRespDto.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.userRespDto.metamask").description("회원 메타마스크 주소"),
                                fieldWithPath("data.write.title").description("제목"),
                                fieldWithPath("data.write.content").description("제목"),
                                fieldWithPath("data.amount").description("금액"),
                                fieldWithPath("data.category").description("카테로리"),
                                fieldWithPath("data.state").description("포스팅 상태"),
                                fieldWithPath("data.postDetailImages").description("이미지"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("존재하는 게시물 번호를 통한 단건 조회요청")
    void 존재하는_게시물_번호를_통한_단건_조회요청() throws Exception {
        //given
        given(postService.findById(1L)).willReturn(게시물_단건조회_응답());

        //expected
        mockMvc.perform(get("/api/post/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(document("post-getOne",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.postId").description("포스팅 ID"),
                                fieldWithPath("data.userRespDto.id").description("유저 아이디"),
                                fieldWithPath("data.userRespDto.email").description("이메일"),
                                fieldWithPath("data.userRespDto.name").description("이름"),
                                fieldWithPath("data.userRespDto.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.userRespDto.metamask").description("회원 메타마스크 주소"),
                                fieldWithPath("data.write.title").description("제목"),
                                fieldWithPath("data.write.content").description("제목"),
                                fieldWithPath("data.amount").description("금액"),
                                fieldWithPath("data.currentAmount").description("현재까지의 모금액"),
                                fieldWithPath("data.category").description("카테로리"),
                                fieldWithPath("data.state").description("포스팅 상태"),
                                fieldWithPath("data.postDetailImages").description("이미지"),
                                fieldWithPath("data.favoriteCount").description("좋아요 갯수"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )

                ));
    }

    @Test
    @DisplayName("존재하지_않는_게시물번호로_조회시_예외를_던진다")
    void 존재하지_않는_게시물번호로_조회시_예외를_던진다() throws Exception{
        //given
        given(postService.findById(0L)).willThrow(new DonationNotFoundException("포스트를 찾을수 없습니다."));

        //expected
        mockMvc.perform(get("/api/post/{id}", 0L))
                .andExpect(status().isBadRequest())
                .andDo(document("post-getOne_exception",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("실패시 반환하지 않는다"),
                                fieldWithPath("error.errorCode").description("에러코드"),
                                fieldWithPath("error.errorMessage").description("에러 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("존재하는_게시물을_수정한다.")
    void 존재하는_게시물을_수정한다() throws Exception {
        //given
        willDoNothing().given(postService).update(게시물_수정_DTO(), 회원검증(1L), 1L);

        // expected
        mockMvc.perform(put("/api/post/{id}",1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(게시물_수정_DTO()))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("post-update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("존재하는 게시물을 삭제요청")
    void 존재하는_게시물을_삭제요청() throws Exception {
        //given
        Long id = 1L;
        willDoNothing().given(postService).delete(1L, 회원검증(id));
        given(authService.extractMemberId(엑세스_토큰)).willReturn(id);

        // expected
        mockMvc.perform(delete("/api/post/{id}",1L)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print())
                .andDo(document("post-delete",
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
    @DisplayName("게시물 권한을 통한 게시물 페이징 조회 요청")
    void 게시물_권한을_통한_게시물_페이징_조회_요청() throws  Exception{
        //given
        List<PostListRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 게시물_전체조회_응답(i, createUser(i)))
                .collect(Collectors.toList());
        PageCustom<PostListRespDto> response = new PageCustom<>(PageableExecutionUtils.getPage(content, PageRequest.of(0, 10), () -> 20L));
        given(postService.getList(any(), any())).willReturn(response);

        // expected
        mockMvc.perform(get("/api/post?page=0&size=10"))
                .andExpect(status().isOk())
                .andDo(document("post-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("자신이 작성한 게시물 페이징 조회")
    void 자신이_작성한_게시물_페이징_조회() throws Exception{
        //given
        List<PostListRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 게시물_전체조회_응답(i, createUser(1L)))
                .collect(Collectors.toList());

        PageCustom<PostListRespDto> response = new PageCustom<>(PageableExecutionUtils.getPage(content, PageRequest.of(0, 10), () -> 20L));
        given(postService.getUserIdList(회원검증(1L), PageRequest.of(0, 10))).willReturn(response);

        // expected
        mockMvc.perform(get("/api/post/{id}/my-page?page=0&size=10",1L))
                .andExpect(status().isOk())
                .andDo(document("post-getMyPostList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}