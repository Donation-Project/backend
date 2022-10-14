package com.donation.repository.post;

import com.donation.domain.entites.Post;
import com.donation.domain.enums.PostState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    Optional<Post> findByUserIdAndWriteTitle(Long id, String title);

    Optional<Post> findByIdAndUserId(Long id, Long user_id);

    Optional<Post> findByIdAndStateNotAndStateNot(Long id, PostState state, PostState state2);

}



