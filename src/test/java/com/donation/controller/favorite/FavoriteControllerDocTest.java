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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.springdocs.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class FavoriteControllerDocTest {
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

        mockMvc.perform(post("/api/favorite")
                        .param("postId", postId.toString())
                        .param("userId", userId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("favorite-save",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("postId").description("포스팅 ID"),
                                parameterWithName("userId").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )

                ));
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
        favoriteService.saveAndCancel(postId, userId);

        // expected
        mockMvc.perform(post("/api/favorite")
                        .param("postId", postId.toString())
                        .param("userId", userId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("favorite-cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        //clear
        favoriteService.deletePostId(postId);
    }

    @Test
    @DisplayName("좋아요(컨트롤러) : 전체 조회")
    void list() throws Exception{
        //given
        List<User> users = IntStream.range(1, 11)
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
        mockMvc.perform(get("/api/favorite")
                        .param("postId",post.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data[9].id").value(users.get(9).getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("favorite-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("postId").description("포스팅 ID")
                        )
                ));
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
                .andDo(document("favorite-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("postId").description("포스팅 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )

                ));
    }

}
