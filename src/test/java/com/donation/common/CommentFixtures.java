package com.donation.common;

import com.donation.domain.comment.dto.CommentSaveReqDto;
import com.donation.domain.comment.entity.Comment;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;

public class CommentFixtures {
    public static String 일반_댓글 = "일반 댓글 입니다.";
    public static String 일반_대댓글 = "일반 대댓글 입니다.";

    /* 댓글 생성 */
    public static CommentSaveReqDto 댓글_생성_DTO(String message){
        return CommentSaveReqDto.builder()
                .message(message)
                .build();
    }

    public static Comment createParentComment() {
        return Comment.parent(createUser(1L), createPost(1L), 일반_댓글);
    }

    public static Comment createParentComment(User user, Post post) {
        return Comment.parent(user, post, 일반_댓글);
    }

    public static Comment createChildComment(Comment parent) {
        return Comment.child(createUser(2L), createPost(1L), 일반_대댓글, parent);
    }

    public static Comment createChildComment(User user, Post post, Comment parent) {
        return Comment.child(user, post, 일반_대댓글, parent);
    }

    public static List<Comment> createParentCommentList(int startNum, int lastNum, User user, Post post) {
        return IntStream.range(startNum, lastNum)
                .mapToObj(i -> createParentComment(user, post))
                .collect(Collectors.toList());
    }

    public static List<Comment> createChildCommentList(int startNum, int lastNum, User user, Post post, Comment parent) {
        return IntStream.range(startNum, lastNum)
                .mapToObj(i -> createChildComment(user, post, parent))
                .collect(Collectors.toList());
    }

}
