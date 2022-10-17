package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.embed.Write;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.domain.enums.PostState.*;
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
        Post post = postRepository.save(getPost());
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

    /**
     *     public PostFindRespDto findById(Long postId) {
     *         return postRepository.findDetailPostById(postId)
     *                 .orElseThrow(IllegalArgumentException::new);
     *     }
     */

    @Test
    @DisplayName("포스트(서비스) : 단건조회")
    void findById(){
        //given
        Post post = postRepository.save(getPost());

        //when
        PostFindRespDto findRespDto = postService.findById(post.getId());

        //then
        assertThat(findRespDto.getPostId()).isEqualTo(post.getId());
        assertThat(findRespDto.getWrite().getTitle()).isEqualTo(post.getWrite().getTitle());
        assertThat(findRespDto.getAmount()).isEqualTo(post.getAmount());
    }

    @Test
    @DisplayName("포스트(서비스) : 전체 조회")
    void getList(){
        //given
        List<Post> posts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .write(new Write("title" + i, "content" + i))
                        .amount(i)
                        .state(WAITING)
                        .category(ETC)
                        .build()
                )
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        Slice<PostListRespDto> postList = postService.getList(pageable, WAITING);

        //then
        assertThat(postList.getSize()).isEqualTo(10);
        assertThat(postList.getNumberOfElements()).isEqualTo(10);
        assertThat(postList.getContent().get(0).getWrite().getTitle()).isEqualTo(posts.get(0).getWrite().getTitle());
        assertThat(postList.getContent().get(0).getWrite().getContent()).isEqualTo(posts.get(0).getWrite().getContent());
    }
}
