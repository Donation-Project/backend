package com.donation.repository.postdetailimage;

import com.donation.domain.entites.PostDetailImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostDetailImageRepository extends JpaRepository<PostDetailImage, Long> {
    @Query("select p.imagePath from PostDetailImage p where p.post.id = :id")
    List<String> findImagePath(@Param("id") Long postId);
}
