package com.donation.controller.Admin;

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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ConstConfig config;

    Post getPost(User user) {
        String title = "title";
        String content = "content";
        Write write = Write.builder()
                .content(content)
                .title(title)
                .build();

        int amount = 10;
        Category category = Category.ETC;
        PostState postState=PostState.WAITING;
        return Post.builder()
                .user(user)
                .write(write)
                .amount(amount)
                .category(category)
                .state(postState)
                .build();
    }

    @BeforeEach
    void init(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("관리자(컨트롤러) : 대기글 수락 및 삭제 ")
    void checkingPost() throws Exception{
        //given

        User user = userRepository.findById(1L).get();
        Post post = getPost(user);
        Post save = postRepository.save(post);
        postRepository.save(post);
        String approval = String.format("/api/Admin/%d?postState=APPROVAL",save.getId());
        String delete = String.format("/api/Admin/%d?postState=DELETE",save.getId());
        //expected
        mockMvc.perform(put(approval))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        Post approvalPost = postRepository.findById(save.getId()).get();
        Assertions.assertThat(approvalPost.getState()).isEqualTo(PostState.APPROVAL);

        mockMvc.perform(put(delete))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        Post deletePost = postRepository.findById(save.getId()).get();
        Assertions.assertThat(deletePost.getState()).isEqualTo(PostState.DELETE);
    }



    private static Write getWrite() {
        String title = "title";
        String content = "content";
        Write write = Write.builder()
                .content(content)
                .title(title)
                .build();
        return write;
    }







}