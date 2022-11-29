package com.donation.domain.notification.handler;

import com.donation.common.utils.ServiceTest;
import com.donation.domain.comment.application.CommentService;
import com.donation.domain.donation.application.DonationService;
import com.donation.domain.favorite.application.FavoriteService;
import com.donation.domain.notification.repository.NotificationRepository;
import com.donation.domain.post.application.PostService;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.CommentFixtures.댓글_생성_DTO;
import static com.donation.common.CommentFixtures.일반_댓글;
import static com.donation.common.DonationFixtures.기부_생성_DTO;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.PostFixtures.게시물_생성_DTO;
import static com.donation.common.UserFixtures.createUser;
import static com.donation.common.UserFixtures.일반_사용자1_이메일;

public class NotificationEventHandlerTest extends ServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private DonationService donationService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationRepository notificationRepository;


    @Test
    @DisplayName("게시물 등록시 등록자에게 알림이 전달된다.")
    void 게시물_등록시_등록자에게_알림이_전달된다(){
        //given
        Long userId = userRepository.save(createUser()).getId();

        //when
        postService.createPost(게시물_생성_DTO(), 회원검증(userId));
        boolean actual = notificationRepository.existsByUserIdAndConformIsFalse(userId);

        //then
        Assertions.assertThat(actual).isTrue();
    }


    @Test
    @DisplayName("게시물에 기부시 게시물 작성자에게 알림이 발송된다.")
    void 게시물_기부시_게시물_작성자에게_알림이_발송된다(){
        //given
        User toUser = userRepository.save(createUser());
        User fromUser = userRepository.save(createUser(일반_사용자1_이메일));
        Post post = postRepository.save(createPost(toUser));

        //when
        donationService.createDonate(회원검증(fromUser.getId()), post.getId(), 기부_생성_DTO());
        boolean actual = notificationRepository.existsByUserIdAndConformIsFalse(toUser.getId());

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("댓글 작성시 게시물 작성자에게 알림이 발송된다.")
    void 댓글_작성시_게시물_작성자에게_알림이_발송된다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when
        commentService.saveComment(post.getId(), 회원검증(user.getId()), 댓글_생성_DTO(일반_댓글));
        boolean actual = notificationRepository.existsByUserIdAndConformIsFalse(user.getId());

        //then
        Assertions.assertThat(actual).isTrue();
    }


    @Test
    @DisplayName("대댓글 작성시 댓글 작성자에게 알림이 발송된다.")
    void 대댓글_작성시_댓글_작성자에게_알림이_발송된다(){
        //given
        User toUser = userRepository.save(createUser());
        User fromUser = userRepository.save(createUser(일반_사용자1_이메일));
        Post post = postRepository.save(createPost(toUser));
        Long commentId = commentService.saveComment(post.getId(), 회원검증(toUser.getId()), 댓글_생성_DTO(일반_댓글));


        //when
        commentService.saveReply(commentId, 회원검증(fromUser.getId()), 댓글_생성_DTO(일반_댓글));
        boolean actual = notificationRepository.existsByUserIdAndConformIsFalse(toUser.getId());

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("좋아요 요청시 게시물 작성자에게 알림이 발송된다.")
    void 좋아요_요청시_게시물_작성자에게_알림이_발송된다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));

        //when
        favoriteService.save(회원검증(user.getId()),post.getId());
        boolean actual = notificationRepository.existsByUserIdAndConformIsFalse(user.getId());

        //then
        Assertions.assertThat(actual).isTrue();
    }
}
