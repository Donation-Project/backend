package com.donation.repository.comment;

import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findCommentByPostAndParentIsNull(final Post post);

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
