package com.donation.repository.comment;

import com.donation.domain.entites.Comment;
import com.donation.domain.entites.Post;
import com.donation.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findCommentByPostAndParentIsNull(Post post);

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findRepliesByParent(Comment parent);

    boolean existsById(final Long id);

    void deleteByPost(Post post);

    default void validateExistsById(final Long id){
        if (!existsById(id)){
            throw new DonationNotFoundException("존재하지 않는 댓글입니다.");
        }
    }
}
