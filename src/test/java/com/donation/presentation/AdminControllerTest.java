package com.donation.presentation;

import com.donation.common.utils.ControllerTest;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.service.PostService;
import com.donation.infrastructure.support.PageCustom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.PostFixtures.게시물_전체조회_응답;
import static com.donation.common.UserFixtures.createUser;
import static com.donation.domain.post.entity.PostState.APPROVAL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminController.class)
class AdminControllerTest extends ControllerTest {

    @MockBean
    private PostService postService;
    @MockBean
    private AuthService authService;


    @Test
    @DisplayName("관리자(컨트롤러) : 대기글 수락 및 삭제 ")
    void confirm() throws Exception {
        //given
        Post post = createPost();
        //expected
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/admin/{id}?postState=APPROVAL", post.getId()))
                .andExpect(status().isOk())
                .andDo(document("admin-confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        requestParameters(
                                parameterWithName("postState").description("포스트 상태")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )));
    }

    @Test
    @DisplayName("관리자(컨트롤러) : 대기글 전체보기 ")
    void getList() throws Exception {
        //given
        List<PostListRespDto> content = LongStream.range(1, 11)
                .mapToObj(i -> 게시물_전체조회_응답(i, createUser(i)))
                .collect(Collectors.toList());
        PageCustom<PostListRespDto> response = new PageCustom<>(PageableExecutionUtils.getPage(content, PageRequest.of(0, 10), () -> 20L));
        given(postService.getList(any(), any())).willReturn(response);
        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/admin/{state}?page=0&size=10", APPROVAL))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("admin-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

}