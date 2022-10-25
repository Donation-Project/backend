package com.donation.controller.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import static com.donation.testutil.TestDtoDataFactory.createPostSaveReqDto;
import static com.donation.testutil.TestDtoDataFactory.createPostUpdateReqDto;
import static com.donation.testutil.TestEntityDataFactory.createPost;
import static com.donation.testutil.TestEntityDataFactory.createUser;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.springdocs.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConstConfig config;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("포스트(RestDocs) : 생성")
    void save() throws Exception {
        //given
        PostSaveReqDto dto = createPostSaveReqDto("title", "content");
        User user = userRepository.save(createUser());

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/post/{id}", user.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.userRespDto.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.userRespDto.name").value(user.getName()))
                .andExpect(jsonPath("$.data.userRespDto.profileImage").value(user.getProfileImage()))
                .andExpect(jsonPath("$.data.write.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.data.write.content").value(dto.getContent()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("post-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.postId").description("포스팅 ID"),
                                fieldWithPath("data.userRespDto.id").description("유저 아이디"),
                                fieldWithPath("data.userRespDto.username").description("이메일"),
                                fieldWithPath("data.userRespDto.name").description("이름"),
                                fieldWithPath("data.userRespDto.profileImage").description("회원 프로필 이미지"),
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
    @DisplayName("포스트(RestDocs) : 단건 조회")
    void get() throws Exception {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));


        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/post/{id}", post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.postId").value(post.getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("post-getOne",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("포스트 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.postId").description("포스팅 ID"),
                                fieldWithPath("data.userRespDto.id").description("유저 아이디"),
                                fieldWithPath("data.userRespDto.username").description("이메일"),
                                fieldWithPath("data.userRespDto.name").description("이름"),
                                fieldWithPath("data.userRespDto.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("data.write.title").description("제목"),
                                fieldWithPath("data.write.content").description("제목"),
                                fieldWithPath("data.amount").description("금액"),
                                fieldWithPath("data.category").description("카테로리"),
                                fieldWithPath("data.state").description("포스팅 상태"),
                                fieldWithPath("data.postDetailImages").description("이미지"),
                                fieldWithPath("data.favoriteCount").description("좋아요 갯수"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )

                ));
    }

    @Test
    @DisplayName("포스트(RestDocs) : 수정")
    void update() throws Exception {
        //given
        Post post = postRepository.save(createPost());
        PostUpdateReqDto request = createPostUpdateReqDto("수정 제목", "수정 내용");

        // expected
        mockMvc.perform(put("/api/post/{id}", post.getId())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
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
    @DisplayName("포스트(RestDocs) : 삭제")
    void delete() throws Exception {
        //given
        Post post = postRepository.save(createPost());

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/post/{id}", post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("post-delete",
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
    @DisplayName("포스트(RestDocs) : 리스트 조회")
    void getList() throws Exception {
        //given
        User user = userRepository.save(createUser());
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> createPost(user, "title" + i, "content"+i))
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        // expected
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
