package com.donation.controller.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.donation.domain.enums.Category.ETC;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    @DisplayName("포스트(컨트롤러) : 회원 가입")
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
}