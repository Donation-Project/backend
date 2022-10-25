package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
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

import static com.donation.domain.enums.PostState.APPROVAL;
import static com.donation.testutil.TestDtoDataFactory.createPostSaveReqDto;
import static com.donation.testutil.TestDtoDataFactory.createPostUpdateReqDto;
import static com.donation.testutil.TestEntityDataFactory.createPost;
import static com.donation.testutil.TestEntityDataFactory.createUser;
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

    @AfterEach
    void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("포스트(서비스) : 생성")
    void save(){
        //given
        PostSaveReqDto request = createPostSaveReqDto("title", "content");
        User user = userRepository.save(createUser());

        //when
        PostSaveRespDto savePost = postService.save(request,user.getId());

        //then
        assertThat(savePost.getWrite().getTitle()).isEqualTo("title");
        assertThat(savePost.getWrite().getContent()).isEqualTo("content");
    }

    @Test
    @DisplayName("포스트(서비스) : 업데이트")
    void update(){
        //given
        Post post = postRepository.save(createPost("title","content"));
        PostUpdateReqDto updateReqDto = createPostUpdateReqDto("update", "update");

        //when
        postService.update(updateReqDto, post.getId());

        //then
        Post findPost = postRepository.findById(post.getId()).get();
        assertThat(findPost.getWrite().getTitle()).isEqualTo(updateReqDto.getTitle());
        assertThat(findPost.getWrite().getContent()).isEqualTo(updateReqDto.getContent());
    }


    @Test
    @DisplayName("포스트(서비스) : 업데이트_예외발생")
    void update_exception(){
        //given
        Long postId = 1L;

        //when
        assertThatThrownBy(() -> postService.findDetailById(postId))
                .isInstanceOf(DonationNotFoundException.class);
    }


    @Test
    @DisplayName("포스트(서비스) : 삭제")
    void delete(){
        //given
        Post post = postRepository.save(createPost());

        //when
        postService.delete(post.getId());

        //then
        assertThatThrownBy(() -> postService.findDetailById(post.getId()))
                .isInstanceOf(DonationNotFoundException.class);
    }

    @Test
    @DisplayName("포스트(서비스) : 단건조회")
    void findById(){
        //given
        Post post = postRepository.save(createPost());

        //when
        PostFindRespDto findRespDto = postService.findDetailById(post.getId());

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
                .mapToObj(i -> createPost("title" + i, "content" + i))
                .collect(Collectors.toList());
        postRepository.saveAll(posts);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        Slice<PostListRespDto> postList = postService.getList(pageable, APPROVAL);

        //then
        assertThat(postList.getSize()).isEqualTo(10);
        assertThat(postList.getNumberOfElements()).isEqualTo(10);
        assertThat(postList.getContent().get(0).getWrite().getTitle()).isEqualTo(posts.get(0).getWrite().getTitle());
        assertThat(postList.getContent().get(0).getWrite().getContent()).isEqualTo(posts.get(0).getWrite().getContent());
    }
}
