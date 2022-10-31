package com.donation.repository.comment;


import com.donation.common.utils.RepositoryTest;
import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.donation.common.CommentFixtures.*;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class CommentRepositoryTest extends RepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("댓글을 정상적으로 저장한다")
    void 댓글을_정상적으로_저장한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when & then
        assertDoesNotThrow(() -> commentRepository.save(createParentComment(user, post)));
    }

    @Test
    @DisplayName("존재하는 댓글이 아니면 예외를 던진다")
    void 존재하는_댓글이_아니면_예외를_던진다(){
        //given
        Long id = 0L;

        //when & then
        assertThatThrownBy(() -> commentRepository.validateExistsById(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @Test
    @DisplayName("존재하는 댓글을 댓글ID를 통해 검증한다.")
    void 존재하는_댓글을_댓글ID를_통해_검증한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();

        //when
        assertDoesNotThrow(() -> commentRepository.validateExistsById(commentId));
    }

    @Test
    @DisplayName("존재하는 댓글아닐때 ID로 조회시 예외를 던진다")
    void 존재하는_댓글아아닐때_ID로_조회시_예외를_던진다(){
        //given
        Long id = 0L;

        //when & then
        assertThatThrownBy(() -> commentRepository.getById(id))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다.");
    }

    @Test
    @DisplayName("최상위 댓글을 게시물 정보를 통해 조회 한다")
    void 최상위_댓글을_게시물_정보를_통해_조회_한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        commentRepository.saveAll(createParentCommentList(0, 10, user, post));

        //when
        List<Comment> actual = commentRepository.findCommentByPostIdAndParentIsNull(post.getId());

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(10);
            assertThat(actual.get(0).getUser()).isEqualTo(user);
        });
    }

    @Test
    @DisplayName("대댓글을 부모 댓글 정보를 통해 조회한다.")
    void 대댓글을_부모_댓글_정보를_통해_조회한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Comment parent = commentRepository.save(createParentComment(user, post));
        commentRepository.saveAll(createChildCommentList(0, 10, user, post, parent));

        //when
        List<Comment> actual = commentRepository.findRepliesByParent(parent);

        //then
        assertAll(() -> {
            assertThat(actual.size()).isEqualTo(10);
            assertThat(actual.get(0).getUser()).isEqualTo(user);
            assertThat(actual.get(0).getParent()).isEqualTo(parent);
        });
    }

    @Test
    @DisplayName("저장된 댓글 정보를 포스트정보를 통해 모두 삭제한다.")
    void 저장된_댓글_정보를_포스트정보를_통해_모두_삭제한다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Comment parent = commentRepository.save(createParentComment(user, post));
        commentRepository.saveAll(createParentCommentList(0, 5, user, post));
        commentRepository.saveAll(createChildCommentList(0, 5, user, post, parent));

        //when
        commentRepository.deleteByPost(post);

        //then
        assertThat(commentRepository.count()).isEqualTo(0);
    }
}
