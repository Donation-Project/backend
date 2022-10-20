package com.donation.repository.favorite;

import com.donation.domain.entites.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorites, Long> {
    void deleteAllByPost_Id(Long post_id);
    void deleteByPost_IdAndUser_Id(Long post_id, Long user_id);
}
