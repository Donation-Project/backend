package com.donation.service.comment;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.comment.CommentRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
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
    void 댓글_정삭적으로_작성이된다(){
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
    void 대댓글이_정상적으로_작성이된다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        Long commentId = commentRepository.save(createParentComment(user, post)).getId();

        //when
        Long actual = commentService.saveReply(commentId, user.getId(), 댓글_생성_DTO(일반_대댓글));

        //then
        assertThat(commentRepository.existsById(actual)).isTrue();
    }
//
//    @Test
//    @DisplayName("대댓글에 댓글을 추가적으로 작성할 경우 예외가 발생한다.")
//    void
}

