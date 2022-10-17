package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.donation.domain.enums.Category.ETC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


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
                .build();
    }

    Post getPost() {
        PostSaveReqDto request = new PostSaveReqDto("title", "content", 1, ETC);
        User user = userRepository.save(getUser());
        return postRepository.save(request.toPost(user));
    }


    @Test
    @DisplayName("포스트(서비스) : 생성")
    void save(){
        //given
        PostSaveReqDto request = new PostSaveReqDto("title", "content", 1, ETC);
        User user = userRepository.save(getUser());

        //when
        PostSaveRespDto savePost = postService.save(request, user.getId());

        //then
        assertThat(savePost.getWrite().getTitle()).isEqualTo("title");
        assertThat(savePost.getWrite().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("포스트(서비스) : 업데이트")
    void update(){
        //given
        Post post = getPost();
        PostUpdateReqDto updateReqDto = new PostUpdateReqDto("title1", "content1", 1, ETC);

        //when
        postService.update(updateReqDto, post.getId());

        //then
        Post findPost = postRepository.findById(post.getId()).get();
        assertThat(findPost.getWrite().getTitle()).isEqualTo("title1");
        assertThat(findPost.getWrite().getContent()).isEqualTo("content1");
    }


    @Test
    @DisplayName("포스트(서비스) : 업데이트_예외발생")
    void update_exception(){
        //given
        Long postId = 1L;

        //when
        assertThatThrownBy(() -> postService.findById(postId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    /**
     *     public void delete(Long postId) {
     *         Post post = postRepository.findById(postId)
     *                 .orElseThrow(IllegalArgumentException::new);
     *         postRepository.delete(post);
     *     }
     */
    @Test
    @DisplayName("포스트(서비스) : 삭제")
    void delete(){
        //given
        Post post = postRepository.save(getPost());

        //when
        postService.delete(post.getId());

        //then
        assertThatThrownBy(() -> postService.findById(post.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
