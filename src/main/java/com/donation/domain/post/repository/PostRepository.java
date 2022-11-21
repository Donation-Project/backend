package com.donation.domain.post.repository;

import com.donation.domain.post.entity.Post;
import com.donation.domain.post.entity.PostState;
import com.donation.global.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    boolean existsById(final Long id);
    @EntityGraph(attributePaths = {"user","postDetailImages", "favorites"})
    Post findDetailById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Post p where p.id = :id")
    Post findByIdWithLock(Long id);

    List<Post> findAllByUpdateAtLessThanEqualAndState(LocalDateTime updateAd, PostState state);

    default Post getById(final Long id){
        return findById(id)
                .orElseThrow(() -> new DonationNotFoundException("존재하지 않는 게시글입니다."));
    }
    default void validateExistsById(final Long id){
        if (!existsById(id)){
            throw new DonationNotFoundException("존재하지 않는 게시글입니다.");
        }
    }
}



