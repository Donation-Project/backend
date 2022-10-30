package com.donation.repository.post;

import com.donation.domain.entites.Post;
import com.donation.domain.enums.PostState;
import com.donation.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    List<Post> findAllByUpdateAtLessThanEqualAndState(LocalDateTime updateAd, PostState state);

    boolean existsById(final Long id);

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



