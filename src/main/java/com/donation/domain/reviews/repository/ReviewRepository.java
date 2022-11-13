package com.donation.domain.reviews.repository;

import com.donation.domain.reviews.entity.Reviews;
import com.donation.global.exception.DonationDuplicateException;
import com.donation.global.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    default Reviews getById(Long id){
        return findById(id)
                .orElseThrow(() -> new DonationNotFoundException("해당 검색결과로 존재햐는 글이 없습니다."));
    }

    Optional<Reviews> findByPostId(Long postId);

    default Reviews getByPostId(Long postId){
        return findByPostId(postId)
                .orElseThrow(() -> new DonationNotFoundException("해당 검색결과로 존재햐는 글이 없습니다."));
    }
    boolean existsByPostId(Long postId);

    default void validateExistsPostId(Long postId){
        if(existsByPostId(postId))
            throw new DonationDuplicateException("이미 작성한 글이 있습니다.");
    }
}
