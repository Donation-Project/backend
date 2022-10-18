package com.donation.controller.admin;

import com.donation.config.ConstConfig;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.donation.domain.enums.Role;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.domain.enums.PostState.APPROVAL;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.springdocs.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class AdminControllerDocTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ConstConfig config;
    User getUser() {
        String username = "username1@naver.com";
        String name = "장원진";
        String password = "1234";
        Role role = Role.USER;

        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .profileImage(config.getBasicImageProfile())
                .role(role)
                .build();
    }

    Post getPost() {
        User user = getUser();
        userRepository.save(user);

        return Post.builder()
                .user(user)
                .write(new Write("title","content"))
                .amount(10)
                .category(Category.ETC)
                .state(PostState.WAITING)
                .build();
    }

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("관리자(RestDocs) : 대기글 수락 및 삭제 ")
    void confirm() throws Exception {
        //given
        Post save = postRepository.save(getPost());

        //expected
        mockMvc.perform(post("/api/admin/{id}?postState=APPROVAL", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("confirm-post",
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
    @DisplayName("관리자(RestDocs) : 리스트 조회")
    void getList() throws Exception {
        //given
        User user = userRepository.save(getUser());
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .write(new Write("title" + i, "content" + i))
                        .amount(i)
                        .state(APPROVAL)
                        .user(user)
                        .category(ETC)
                        .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(posts);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/post?page=0&size=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content[0].postId").value(posts.get(0).getId()))
                .andExpect(jsonPath("$.data.content[0].write.title").value(posts.get(0).getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[0].write.content").value(posts.get(0).getWrite().getContent()))
                .andExpect(jsonPath("$.data.content[9].postId").value(posts.get(9).getId()))
                .andExpect(jsonPath("$.data.content[9].write.title").value(posts.get(9).getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[9].write.content").value(posts.get(9).getWrite().getContent()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("post-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }



}