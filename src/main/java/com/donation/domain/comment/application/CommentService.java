package com.donation.domain.comment.application;

import com.donation.domain.comment.event.NewCommentEvent;
import com.donation.domain.comment.event.NewReplyEvent;
import com.donation.presentation.auth.LoginMember;
import com.donation.domain.comment.dto.CommentSaveReqDto;
import com.donation.domain.comment.dto.CommentResponse;
import com.donation.domain.comment.dto.ReplyResponse;
import com.donation.domain.comment.entity.Comment;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.global.exception.DonationInvalidateException;
import com.donation.domain.comment.repository.CommentRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long saveComment(final Long postId, final LoginMember loginMember, final CommentSaveReqDto commentSaveReqDto){
        Post post = postRepository.getById(postId);
        User user = userRepository.getById(loginMember.getId());
        Comment comment = commentRepository.save(Comment.parent(user, post, commentSaveReqDto.getMessage()));
        publisher.publishEvent(new NewCommentEvent(post.getId(), user.getId(), postId, comment.getId()));
        return comment.getId();
    }

    @Transactional
    public Long saveReply(final Long commentId, final LoginMember loginMember, final CommentSaveReqDto commentSaveReqDto){
        Comment parent = commentRepository.getById(commentId);
        Comment comment = commentRepository.save(validateReplySave(parent, loginMember.getId(), commentSaveReqDto));
        publisher.publishEvent(new NewReplyEvent(parent.getUser().getId(), loginMember.getId(), parent.getPost().getId(), comment.getId()));
        return comment.getId();
    }

    private Comment validateReplySave(final Comment parent, final Long userId, final CommentSaveReqDto commentSaveReqDto) {
        if (!parent.isParent())
            throw new DonationInvalidateException("대댓글에는 답글을 달 수 없습니다.");
        return Comment.child(userRepository.getById(userId), parent.getPost(), commentSaveReqDto.getMessage(), parent);
    }

    public List<CommentResponse> findComment(final Long postId){
        return commentRepository.findCommentByPostIdAndParentIsNull(postId).stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse convertToCommentResponse(Comment comment){
        if(comment.isSoftRemoved()){
            return CommentResponse.of(comment, convertToRepliesResponse(comment));
        }
        return CommentResponse.of(comment, comment.getUser(), convertToRepliesResponse(comment));
    }

    public List<ReplyResponse> convertToRepliesResponse(Comment parent){
        return commentRepository.findRepliesByParent(parent).stream()
                .map(comment -> ReplyResponse.of(comment, comment.getUser()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(final Long commentId, final LoginMember loginMember){
        Comment comment = commentRepository.getById(commentId);
        comment.validateOwner(loginMember.getId());
        deleteDelegate(comment);
    }

    private void deleteDelegate(Comment comment){
        if(comment.isParent()){
            deleteParent(comment);
            return;
        }
        deleteChild(comment);
    }
    private void deleteParent(Comment comment) {
        if (comment.hasNoReply()) {
            commentRepository.delete(comment);
            return;
        }
        comment.changePretendingToBeRemoved();
    }

    private void deleteChild(Comment comment) {
        Comment parent = comment.getParent();
        parent.deleteChild(comment);
        commentRepository.delete(comment);

        if (parent.hasNoReply() && parent.isSoftRemoved()) {
            commentRepository.delete(parent);
        }
    }
}
