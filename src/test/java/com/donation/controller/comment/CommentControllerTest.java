package com.donation.controller.comment;

import com.donation.common.PostFixtures;
import com.donation.common.UserFixtures;
import com.donation.common.response.comment.CommentResponse;
import com.donation.common.response.comment.ReplyResponse;
import com.donation.common.utils.ControllerTest;
import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.service.auth.application.AuthService;
import com.donation.service.comment.CommentService;
import com.donation.web.controller.comment.CommentController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Collectors;

import static com.donation.common.AuthFixtures.*;
import static com.donation.common.CommentFixtures.*;
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

@WebMvcTest(CommentController.class)
public class CommentControllerTest extends ControllerTest {

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("댓글요청이 정상적으로 등록된다.")
    void 댓글요청이_정상적으로_등록된다() throws Exception {
        //given
        Long userId = 1L;
        Long postId = 1L;

        given(commentService.saveComment(postId, 회원검증(userId), 댓글_생성_DTO(일반_댓글))).willReturn(1L);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);

        //expect
        mockMvc.perform(post("/api/post/{id}/comment", postId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(댓글_생성_DTO(일반_댓글)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print())
                .andDo(document("comment-save",
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
    @DisplayName("대댓글요청이 정상적으로 등록된다.")
    void 대댓글요청이_정상적으로_등록된다() throws Exception {
        //given
        Long userId = 1L;
        Long commentId = 1L;

        given(commentService.saveReply(commentId, 회원검증(userId), 댓글_생성_DTO(일반_댓글))).willReturn(1L);
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);

        //expect
        mockMvc.perform(post("/api/comment/{id}/reply", commentId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(댓글_생성_DTO(일반_대댓글)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("reply-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("댓글 ID")
                        )
                ));
    }

    @Test
    @DisplayName("댓글 삭제 요청이 정상적으로 동작한다.")
    void 댓글_삭제_요청이_정상적으로_동작한다() throws Exception{
        //given
        Long userId = 1L;
        Long commentId = 1L;

        willDoNothing().given(commentService).delete(commentId,회원검증(userId));
        given(authService.extractMemberId(엑세스_토큰)).willReturn(userId);

        //expect
        mockMvc.perform(delete("/api/comment/{id}", commentId)
                        .header(AUTHORIZATION_HEADER_NAME, 토큰_정보)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("comment-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT 엑세스 토큰")
                        ),
                        pathParameters(
                                parameterWithName("id").description("댓글 ID")
                        )
                ));
    }

    @Test
    @DisplayName("게시물ID를 통한 댓글 조회 요청이 정상적으로 동작한다")
    void 게시물ID를_통한_댓글_조회_요청이_정상적으로_동작한다() throws Exception{
        //given
        User user = UserFixtures.createUser(1L);
        Post post = PostFixtures.createPost(UserFixtures.createUser(1L));
        Comment parent = createParentComment(user, post);
        List<ReplyResponse> replies = createChildCommentList(0, 2, UserFixtures.createUser(2L), post, parent).stream()
                .map(Reply -> ReplyResponse.of(Reply, Reply.getUser()))
                .collect(Collectors.toList());
        given(commentService.findComment(1L)).willReturn(List.of(CommentResponse.of(parent, user, replies)));

        //expect
        mockMvc.perform(get("/api/post/{id}/comment", 1L))
                .andExpect(status().isOk())
                .andDo(document("comment-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data[0].id").description("댓글 ID"),
                                fieldWithPath("data[0].name").description("회원 이름"),
                                fieldWithPath("data[0].profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data[0].content").description("댓글 내용"),
                                fieldWithPath("data[0].createdAt").description("댓글 생성일"),
                                fieldWithPath("data[0].doesExist").description("댓글 삭제 여부"),
                                fieldWithPath("data[0].replies[0].id").description("댓글 ID"),
                                fieldWithPath("data[0].replies[0].name").description("회원 이름"),
                                fieldWithPath("data[0].replies[0].profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data[0].replies[0].content").description("대댓글 내용"),
                                fieldWithPath("data[0].replies[0].createdAt").description("대댓글 생성일"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }
}
