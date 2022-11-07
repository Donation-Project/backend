package com.donation.service.comment;

import com.donation.auth.LoginMember;
import com.donation.common.request.comment.CommentSaveReqDto;
import com.donation.common.response.comment.CommentResponse;
import com.donation.common.response.comment.ReplyResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long saveComment(final Long postId, final LoginMember loginMember, final CommentSaveReqDto commentSaveReqDto){
        Post post = postRepository.getById(postId);
        User user = userRepository.getById(loginMember.getId());
        Comment comment = Comment.parent(user, post, commentSaveReqDto.getMessage());
        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long saveReply(final Long commentId, final LoginMember loginMember, final CommentSaveReqDto commentSaveReqDto){
        Comment child = validateReplySave(commentId, loginMember.getId(), commentSaveReqDto);
        return commentRepository.save(child).getId();
    }

    private Comment validateReplySave(final Long commentId, final Long userId, final CommentSaveReqDto commentSaveReqDto) {
        Comment parent = commentRepository.getById(commentId);
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
