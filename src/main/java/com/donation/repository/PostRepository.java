package com.donation.repository;

import com.donation.domain.entites.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {


    Optional<Post> findByUserIdAndWriteTitle(Long id, String title);

}
