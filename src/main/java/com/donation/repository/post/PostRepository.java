package com.donation.repository.post;

import com.donation.domain.entites.Post;
import com.donation.domain.enums.PostState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {

    List<Post> findAllByUpdateAtLessThanEqualAndState(LocalDateTime updateAd, PostState state);


}



