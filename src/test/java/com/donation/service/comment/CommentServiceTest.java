package com.donation.service.comment;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.comment.CommentRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.CommentFixtures.*;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;

public class CommentServiceTest extends ServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 정상적으로 작성이된다.")
    void 댓글_정삭적으로_작성이된다() {
        //given
        User user = userRepository.save(createUser());
        Long postId = postRepository.save(createPost(user)).getId();

        //when
        Long actual = commentService.saveComment(postId, user.getId(), 댓글_생성_DTO(일반_댓글));

        //then
        assertThat(commentRepository.existsById(actual)).isTrue();
    }

    @Test
    @DisplayName("대댓글이 정상적으로 작성이된다.")
    void 대댓글이_정상적으로_작성이된다() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();

        //when
        Long actual = commentService.saveReply(commentId, user.getId(), 댓글_생성_DTO(일반_대댓글));

        //then
        assertThat(commentRepository.existsById(actual)).isTrue();
    }

    @Test
    @DisplayName("대댓글에 댓글을 추가적으로 작성할 경우 예외가 발생한다.")
    void 대댓글에_댓글을_추가적으로_작성할_경우_예외가_발생한다() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Comment comment = commentRepository.save(createParentComment(user, post));
        Comment reply = commentRepository.save(createChildComment(user, post, comment));

        //when & then
        Assertions.assertThatThrownBy(() -> commentService.saveReply(reply.getId(), user.getId(), 댓글_생성_DTO(일반_대댓글)))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("대댓글에는 답글을 달 수 없습니다.");
    }

    @Test
    @DisplayName("댓글 작성자가 아닌 다른 사람이 삭제 요청시 예외를 던진다.")
    void 댓글_작성자가_아닌_다른_사람이_삭제_요청시_예외를_던진다() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();

        //when & then
        Assertions.assertThatThrownBy(() -> commentService.delete(commentId, 0L))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("댓글의 작성자만 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("대댓글이 존재하지 않는 댓글 삭제 요청 성공")
    void 대댓글이_존재하지_않는_댓글_삭제_요청_성공() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();

        //when
        commentService.delete(commentId, user.getId());

        //then
        assertThat(commentRepository.existsById(commentId)).isFalse();
    }

    @Test
    @DisplayName("대댓글이 존재하는 댓글 삭제 요청 성공")
    void 대댓글이_존재하는_댓글_삭제_요청_성공() {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();
        commentService.saveReply(commentId, user.getId(), 댓글_생성_DTO(일반_대댓글));

        //when
        commentService.delete(commentId, user.getId());
        Comment actual = commentRepository.getById(commentId);

        //then
        assertThat(actual.isSoftRemoved()).isTrue();
    }
}

