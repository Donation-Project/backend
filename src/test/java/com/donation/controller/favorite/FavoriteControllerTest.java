package com.donation.controller.favorite;


import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.donation.service.favorite.FavoriteService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class FavoriteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    Favorites getFavorite(){
        User user = User.builder().build();
        userRepository.save(user);

        Post post = Post.builder()
                .user(user)
                .build();
        postRepository.save(post);

        return Favorites.builder()
                .user(user)
                .post(post)
                .build();
    }

    @AfterEach
    void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 저장")
    void save() throws Exception {
        //given
        Favorites favorite = getFavorite();
        Long postId = favorite.getPost().getId();
        Long userId = favorite.getUser().getId();

        // expected
        mockMvc.perform(post("/api/favorite?postId="+postId + "&userId=" + userId ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        //clear
        favoriteService.deletePostId(postId);
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 취소")
    void cancel() throws Exception {
        //given
        Favorites favorite = getFavorite();
        Long postId = favorite.getPost().getId();
        Long userId = favorite.getUser().getId();
        favoriteService.saveAndCancel(postId, userId);

        // expected
        mockMvc.perform(post("/api/favorite?postId="+postId + "&userId=" + userId ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        //clear
        favoriteService.deletePostId(postId);
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 전체 조회")
    void list() throws Exception{
        //given
        List<User> users = IntStream.range(1, 31)
                .mapToObj(i -> User.builder()
                        .username("username" + i + "@naver.com")
                        .name("name" + i)
                        .password("password" + i)
                        .build()
                )
                .collect(Collectors.toList());
        userRepository.saveAll(users);
        Post post = postRepository.save(Post.builder()
                .user(users.get(0))
                .build());
        users.stream()
                .forEach(u-> {
                    favoriteService.saveAndCancel(post.getId(), u.getId());
                });

        // expected
        mockMvc.perform(get("/api/favorite?postId="+post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data[29].id").value(users.get(29).getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        //clear
        favoriteService.deletePostId(post.getId());
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 포스트 아이디로 삭제 저장된 좋아요 전체 삭제")
    void delete() throws Exception{
        //given
        Favorites favorite = getFavorite();
        Long postId = favorite.getPost().getId();
        Long userId = favorite.getUser().getId();
        favoriteService.saveAndCancel(postId, userId);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/favorite?postId="+postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

}
