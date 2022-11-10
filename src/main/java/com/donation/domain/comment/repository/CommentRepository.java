package com.donation.domain.comment.repository;

import com.donation.domain.comment.entity.Comment;
import com.donation.domain.post.entity.Post;
import com.donation.global.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findCommentByPostIdAndParentIsNull(final Long postId);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findRepliesByParent(final Comment parent);

    boolean existsById(final Long id);

    void deleteByPost(final Post post);

    default void validateExistsById(final Long id){
        if (!existsById(id)){
            throw new DonationNotFoundException("존재하지 않는 댓글입니다.");
        }
    }

    default Comment getById(final Long id){
        return findById(id)
                .orElseThrow(() -> new DonationNotFoundException("존재하지 않는 댓글입니다."));
    }
}
