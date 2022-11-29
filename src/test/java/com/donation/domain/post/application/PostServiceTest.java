package com.donation.domain.post.application;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.post.dto.PostFindRespDto;
import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.dto.PostSaveRespDto;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.global.exception.DonationInvalidateException;
import com.donation.global.exception.DonationNotFoundException;
import com.donation.domain.post.application.Image.ImageService;
import com.donation.infrastructure.util.PageCursor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.PostFixtures.*;
import static com.donation.common.UserFixtures.createUser;
import static com.donation.common.utils.CursorRequestFixtures.createCursor;
import static com.donation.domain.post.entity.PostState.APPROVAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class PostServiceTest extends ServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    @Test
    @DisplayName("게시물 작성 성공")
    void 게시물_작성_성공() {
        //given
        Long id = userRepository.save(createUser()).getId();

        //when
        PostSaveRespDto actual = postService.createPost(게시물_생성_DTO(), 회원검증(id));

        //then
        assertAll(() ->{
            assertThat(actual.getWrite().getTitle()).isEqualTo(일반_게시물_제목);
            assertThat(actual.getWrite().getContent()).isEqualTo(일반_게시물_내용);
            assertThat(actual.getAmount()).isEqualTo(일반_게시물_기부금);
        });

        //clear
        actual.getPostDetailImages().forEach(image -> imageService.delete(image));
    }

    @Test
    @DisplayName("게시물 내용 업데이트 성공")
    void 게시물_내용_업데이트_성공() {
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when
        postService.update(게시물_수정_DTO(),회원검증(user.getId()), id);
        Post actual = postRepository.findById(id).get();

        //then
        assertAll(() ->{
            assertThat(actual.getWrite().getTitle()).isEqualTo(게시물_수정_제목);
            assertThat(actual.getWrite().getContent()).isEqualTo(게시물_수정_내용);
            assertThat(actual.getCategory()).isEqualTo(게시물_수정_카테고리);
            assertThat(actual.getAmount()).isEqualTo(게시물_수정_기부금);
        });
    }

    @Test
    @DisplayName("게시물 내용 업데이트시 작성자가 아니면 오류를 발생한다.")
    void 게시물_내용_업데이트시_작성자가_아니면_오류를_발생한다(){
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();
        Long 오류발생 = 0L;

        //when & then
        Assertions.assertThatThrownBy(() -> postService.update(게시물_수정_DTO(), 회원검증(오류발생), id))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("게시물의 작성자만 권한이 있습니다.");
    }

    @Test
    @DisplayName("존재하는 않는 게시물번호로 단건조회시 예외를 던진다.")
    void 존재하는_않는_게시물번호로_단건조회시_예외를_던진다() {
        //given
        Long id = 0L;

        //when
        assertThatThrownBy(() -> postRepository.getById(id))
                .isInstanceOf(DonationNotFoundException.class);
    }

    @Test
    @DisplayName("존재하는 게시물번호를 통해 게시물 단건조회 성공")
    void 존재하는_게시물번호를_통해_게시물_단건조회_성공() {
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when
        PostFindRespDto findRespDto = postService.findById(id);

        //then
        assertThat(findRespDto.getPostId()).isEqualTo(id);
    }


    @Test
    @DisplayName("게시물 번호를 통해 게시물을 삭제한다")
    void 게시물_번호를_통해_게시물을_삭제한다() {
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when
        postService.delete(id, 회원검증(user.getId()));

        //then
        assertThatThrownBy(() -> postService.findById(id))
                .isInstanceOf(DonationNotFoundException.class);
    }

    @Test
    @DisplayName("게시물 삭제시 작성자가 아니면 오류를 발생한다.")
    void 게시물_삭제시_작성자가_아니면_오류를_발생한다(){
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();
        Long 오류발생 = 0L;

        //when & then
        Assertions.assertThatThrownBy(() -> postService.delete(id,회원검증(오류발생)))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("게시물의 작성자만 권한이 있습니다.");
    }

    @Test
    @DisplayName("회원번호를 통해 게시물을 페이징으로 조회한다.")
    void 회원번호를_통해_게시물을_페이징으로_조회한다() {
        //given
        User user = userRepository.save(createUser());
        List<Post> posts = postRepository.saveAll(creatPostList(1, 11, user));

        //when
        PageCursor<PostListRespDto> postList = postService.getList(회원검증(user.getId()), createCursor());

        //then
        assertAll(() -> {
            assertThat(postList.getRequest().size()).isEqualTo(10);
            assertThat(postList.getRequest().get(0).getWrite().getTitle()).isEqualTo(posts.get(9).getWrite().getTitle());
            assertThat(postList.getRequest().get(0).getWrite().getContent()).isEqualTo(posts.get(9).getWrite().getContent());
        });
    }

    @Test
    @DisplayName("게시물 상태정보를 통해 게시물을 페이징으로 조회한다.")
    void 게시물_상태정보를_통해_게시물을_페이징으로_조회한다(){
        //given
        User user = userRepository.save(createUser());
        List<Post> posts = postRepository.saveAll(creatPostList(1, 11, user));

        //when
        PageCursor<PostListRespDto> actual = postService.getList(createCursor(), APPROVAL);

        //then
        assertAll(() -> {
            assertThat(actual.getRequest().size()).isEqualTo(10);
            assertThat(actual.getRequest().get(0).getWrite().getTitle()).isEqualTo(posts.get(9).getWrite().getTitle());
            assertThat(actual.getRequest().get(0).getWrite().getContent()).isEqualTo(posts.get(9).getWrite().getContent());
            assertThat(actual.getRequest().get(0).getState()).isEqualTo(APPROVAL);
        });
    }

    @Test
    @DisplayName("현재금액을 2 증가시킨다")
    void 현재금액을_2_증가시킨다() {
        //given
        User user = userRepository.save(createUser());
        Long id = postRepository.save(createPost(user)).getId();

        //when
        postService.increase(id, 2f);

        //then
        Assertions.assertThat(postRepository.getById(id).getCurrentAmount()).isEqualTo(2);
    }
}