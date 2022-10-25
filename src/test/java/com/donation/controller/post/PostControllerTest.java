package com.donation.controller.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.testutil.TestDtoDataFactory.createPostSaveReqDto;
import static com.donation.testutil.TestDtoDataFactory.createPostUpdateReqDto;
import static com.donation.testutil.TestEntityDataFactory.createPost;
import static com.donation.testutil.TestEntityDataFactory.createUser;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 포스트 작성")
    void save() throws Exception {
        //given
        PostSaveReqDto dto = createPostSaveReqDto("title", "content");
        User user = userRepository.save(createUser());

        // expected
        mockMvc.perform(post("/api/post/{id}", user.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.userRespDto.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.userRespDto.name").value(user.getName()))
                .andExpect(jsonPath("$.data.userRespDto.profileImage").value(user.getProfileImage()))
                .andExpect(jsonPath("$.data.write.title").value(dto.getTitle()))
                .andExpect(jsonPath("$.data.write.content").value(dto.getContent()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 단건 조회")
    void get() throws Exception {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}",post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.postId").value(post.getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 단건조회 없는 포스트 예외발생")
    void get_exception() throws Exception{
        //given
        Long postId = 1L;

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post/{id}", postId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("NOT_FOUND"))
                .andDo(print());
    }


    @Test
    @DisplayName("포스트(컨트롤러) : 업데이트")
    void update() throws Exception {
        //given
        Post post = postRepository.save(createPost());
        PostUpdateReqDto request = createPostUpdateReqDto("수정 제목", "수정 내용");

        // expected
        mockMvc.perform(put("/api/post/{id}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("포스트(컨트롤러) : 삭제")
    void delete() throws Exception {
        //given
        Post post = postRepository.save(createPost());

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/post/{id}",post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 리스트 조회")
    void getList() throws  Exception{
        //given
        User user = userRepository.save(createUser());
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> createPost(user, "title" + i, "content"+i))
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.data.content[0].postId").value(posts.get(0).getId()))
                .andExpect(jsonPath("$.data.content[0].write.title").value(posts.get(0).getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[0].write.content").value(posts.get(0).getWrite().getContent()))
                .andExpect(jsonPath("$.data.content[9].postId").value(posts.get(9).getId()))
                .andExpect(jsonPath("$.data.content[9].write.title").value(posts.get(9).getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[9].write.content").value(posts.get(9).getWrite().getContent()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }
}