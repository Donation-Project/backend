package com.donation.service.comment;

import com.donation.common.request.comment.CommentSaveReqDto;
import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.comment.CommentRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveComment(final Long postId,final Long userId, final CommentSaveReqDto commentSaveReqDto){
        Post post = postRepository.getById(postId);
        User user = userRepository.getById(userId);
        Comment comment = Comment.parent(user, post, commentSaveReqDto.getMessage());
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long saveReply(final Long commentId, final Long userId, final CommentSaveReqDto commentSaveReqDto){
        Comment parent = commentRepository.getById(commentId);
        User user = userRepository.getById(userId);
        if (!parent.isParent())
            throw new DonationInvalidateException("대댓글에는 답글을 달 수 없습니다.");
        Comment child = Comment.child(user, parent.getPost(), commentSaveReqDto.getMessage(), parent);
        return commentRepository.save(child).getId();
    }

}
