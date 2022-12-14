package com.donation.domain.favorite.repository;

import com.donation.domain.favorite.entity.Favorites;
import com.donation.global.exception.DonationDuplicateException;
import com.donation.global.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorites, Long> {

    @Query("select f.user.id from Favorites f where f.post.id = :postId")
    List<Long> findUserIdByPostId(@Param(value = "postId") Long postId);
    boolean existsByPostIdAndUserId(final Long post_id, final Long user_id);
    void deleteAllByPostId(Long post_id);
    Optional<Favorites> findByPostIdAndUserId(final Long post_id, final Long user_id);
    default Favorites getByPostIdAndUserId(final Long post_id, final Long user_id) {
        return findByPostIdAndUserId(post_id, user_id)
                .orElseThrow(() -> new DonationNotFoundException("존재하지 않는 정보입니다."));
    }
    default void validateExistsByPostIdAndUserId(final Long post_id, final Long user_id){
        if (existsByPostIdAndUserId(post_id, user_id)){
            throw new DonationDuplicateException("이미 좋아요를 누른 회원입니다.");
        }
    }
}
