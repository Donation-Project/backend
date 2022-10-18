package com.donation.controller.admin;

import com.donation.config.ConstConfig;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.Post;

import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;

import com.donation.repository.post.PostRepository;

import com.donation.repository.user.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("관리자(컨트롤러) : 대기글 수락 및 삭제 ")
    void checkingPost() throws Exception {
        //given
        Post post=Post.builder()
                .write(new Write("title","content"))
                .amount(10)
                .category(Category.ETC)
                .state(PostState.WAITING)
                .build();
        postRepository.save(post);
        //expected
        mockMvc.perform(post("/api/admin/{id}?postState=APPROVAL", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자(컨트롤러) : 대기글 전체보기 ")
    void getPostList() throws Exception {


        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .write(new Write("title","content"))
                        .amount(10)
                        .category(Category.ETC)
                        .state(PostState.WAITING)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(posts);
        // expected
        mockMvc.perform(get("/api/admin/WAITING?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content.length()", Matchers.is(10)))
                //user1
                .andExpect(jsonPath("$.data.content[0].postId").value(posts.get(0).getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                //user10
                .andExpect(jsonPath("$.data.content[9].postId").value(posts.get(9).getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

    }

}