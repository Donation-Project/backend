package com.donation.service.comment;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.User;
import com.donation.repository.comment.CommentRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.CommentFixtures.댓글_생성_DTO;
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
    @DisplayName("댓글 정상적으로 작성 완료된다.")
    void 댓글_정삭적으로_작성_완료(){
        //given
        User user = userRepository.save(createUser());
        Long postId = postRepository.save(createPost(user)).getId();

        //when
        Long commentId = commentService.saveComment(postId, user.getId(), 댓글_생성_DTO());

        //then
        assertThat(commentRepository.existsById(commentId)).isTrue();
    }

    @Test
    @DisplayName("대댓글이 정상적으로 작성 완료된다.")
}

