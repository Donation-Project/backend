package com.donation.repository.post;

import com.donation.common.response.post.PostListRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.enums.PostState;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.utils.PageCustom;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    long countByUserId(Long user_id);

    long countByStateIn(List<PostState> state);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Post p where p.id = :id")
    Post findByIdWithOptimisticLock(Long id);

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

    default PageCustom<PostListRespDto> getUserIdPageList(Long userId,Pageable pageable){
        List<PostListRespDto> content = getUserIdPageDtoList(userId, pageable);
        return new PageCustom<>(new PageImpl<>(content, pageable, this.countByUserId(userId)));
    }

    default PageCustom<PostListRespDto> getPageList(Pageable pageable, PostState... postState) {
        List<PostListRespDto> content = getPageDtoAll(pageable, postState);
        return new PageCustom<>(new PageImpl<>(content, pageable, this.countByStateIn(List.of(postState))));
    }
}



