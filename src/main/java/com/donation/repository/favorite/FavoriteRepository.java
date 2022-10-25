package com.donation.repository.favorite;

import com.donation.domain.entites.Favorites;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites, Long> {
    void deleteAllByPost_Id(Long post_id);
    void deleteByPost_IdAndUser_Id(Long post_id, Long user_id);

    @Query("select f.user.id from Favorites f where f.post.id = :postId")
    List<Long> findUserIdByPostId(@Param(value = "postId") Long postId);

    Long countFavoritesByPost_Id(Long post_id);

    Optional<Favorites> findByPost_IdAndUser_Id(Long post_id, Long user_id);

}
