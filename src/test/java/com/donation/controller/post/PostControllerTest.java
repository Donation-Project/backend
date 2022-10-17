package com.donation.controller.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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

import static com.donation.domain.enums.Category.ETC;
import static com.donation.domain.enums.PostState.APPROVAL;
import static com.donation.domain.enums.PostState.WAITING;
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
    private ConstConfig config;

    @Autowired
    private ObjectMapper objectMapper;


    User getUser() {
        String username = "username@naver.com";
        String name = "정우진";
        String password = "1234";
        Role role = Role.USER;

        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .role(role)
                .profileImage(config.getBasicImageProfile())
                .build();
    }

    Post getPost() {
        User user = userRepository.save(getUser());
        return Post.builder()
                .write(new Write("title", "content"))
                .user(user)
                .state(WAITING)
                .category(ETC)
                .amount(1)
                .build();
    }

    @BeforeEach
    void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 포스트 작성")
    void save() throws Exception {
        //given
        PostSaveReqDto data = PostSaveReqDto.builder()
                .title("title")
                .content("content")
                .amount(1)
                .category(ETC)
                .build();

        String request = objectMapper.writeValueAsString(data);
        User user = userRepository.save(getUser());

        // expected
        mockMvc.perform(post("/api/post?id="+user.getId())
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.userRespDto.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.userRespDto.name").value(user.getName()))
                .andExpect(jsonPath("$.data.userRespDto.profileImage").value(user.getProfileImage()))
                .andExpect(jsonPath("$.data.write.title").value(data.getTitle()))
                .andExpect(jsonPath("$.data.write.content").value(data.getContent()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트(컨트롤러) : 단건 조회")
    void get() throws Exception {
        //given
        Post post = postRepository.save(getPost());

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
                .andExpect(jsonPath("$.error.errorCode").value("400"))
                .andDo(print());
    }


    @Test
    @DisplayName("포스트(컨트롤러) : 업데이트")
    void update() throws Exception {
        //given
        Post post = postRepository.save(getPost());
        PostUpdateReqDto dto = PostUpdateReqDto.builder()
                .title("수정제목1")
                .content("수정내용1")
                .category(post.getCategory())
                .amount(post.getAmount())
                .build();
        String request = objectMapper.writeValueAsString(dto);

        // expected
        mockMvc.perform(put("/api/post/{id}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(request)
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
        Post post = postRepository.save(getPost());

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