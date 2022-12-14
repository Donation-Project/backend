package com.donation.presentation;

import com.donation.common.PostFixtures;
import com.donation.common.UserFixtures;
import com.donation.domain.comment.dto.CommentResponse;
import com.donation.domain.comment.dto.ReplyResponse;
import com.donation.common.utils.ControllerTest;
import com.donation.domain.comment.entity.Comment;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.comment.application.CommentService;
import com.donation.presentation.CommentController;
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
    @DisplayName("??????????????? ??????????????? ????????????.")
    void ???????????????_???????????????_????????????() throws Exception {
        //given
        Long userId = 1L;
        Long postId = 1L;

        given(commentService.saveComment(postId, ????????????(userId), ??????_??????_DTO(??????_??????))).willReturn(1L);
        given(authService.extractMemberId(?????????_??????)).willReturn(userId);

        //expect
        mockMvc.perform(post("/api/post/{id}/comment", postId)
                        .header(AUTHORIZATION_HEADER_NAME, ??????_??????)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(??????_??????_DTO(??????_??????)))
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
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT ????????? ??????")
                        ),
                        pathParameters(
                                parameterWithName("id").description("????????? ID")
                        )
                ));
    }

    @Test
    @DisplayName("?????????????????? ??????????????? ????????????.")
    void ??????????????????_???????????????_????????????() throws Exception {
        //given
        Long userId = 1L;
        Long commentId = 1L;

        given(commentService.saveReply(commentId, ????????????(userId), ??????_??????_DTO(??????_??????))).willReturn(1L);
        given(authService.extractMemberId(?????????_??????)).willReturn(userId);

        //expect
        mockMvc.perform(post("/api/comment/{id}/reply", commentId)
                        .header(AUTHORIZATION_HEADER_NAME, ??????_??????)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(??????_??????_DTO(??????_?????????)))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("reply-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT ????????? ??????")
                        ),
                        pathParameters(
                                parameterWithName("id").description("?????? ID")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ????????? ??????????????? ????????????.")
    void ??????_??????_?????????_???????????????_????????????() throws Exception{
        //given
        Long userId = 1L;
        Long commentId = 1L;

        willDoNothing().given(commentService).delete(commentId,????????????(userId));
        given(authService.extractMemberId(?????????_??????)).willReturn(userId);

        //expect
        mockMvc.perform(delete("/api/comment/{id}", commentId)
                        .header(AUTHORIZATION_HEADER_NAME, ??????_??????)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("comment-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName(AUTHORIZATION_HEADER_NAME).description("JWT ????????? ??????")
                        ),
                        pathParameters(
                                parameterWithName("id").description("?????? ID")
                        )
                ));
    }

    @Test
    @DisplayName("?????????ID??? ?????? ?????? ?????? ????????? ??????????????? ????????????")
    void ?????????ID???_??????_??????_??????_?????????_???????????????_????????????() throws Exception{
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
                                parameterWithName("id").description("????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("?????? ??????"),
                                fieldWithPath("data[0].id").description("?????? ID"),
                                fieldWithPath("data[0].name").description("?????? ??????"),
                                fieldWithPath("data[0].profileImage").description("?????? ????????? ?????????"),
                                fieldWithPath("data[0].content").description("?????? ??????"),
                                fieldWithPath("data[0].createdAt").description("?????? ?????????"),
                                fieldWithPath("data[0].doesExist").description("?????? ?????? ??????"),
                                fieldWithPath("data[0].replies[0].id").description("?????? ID"),
                                fieldWithPath("data[0].replies[0].name").description("?????? ??????"),
                                fieldWithPath("data[0].replies[0].profileImage").description("?????? ????????? ?????????"),
                                fieldWithPath("data[0].replies[0].content").description("????????? ??????"),
                                fieldWithPath("data[0].replies[0].createdAt").description("????????? ?????????"),
                                fieldWithPath("error").description("?????? ????????? ?????? ??????")
                        )
                ));
    }
}
