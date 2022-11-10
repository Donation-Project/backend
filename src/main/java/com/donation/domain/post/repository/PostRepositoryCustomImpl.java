package com.donation.domain.post.repository;

import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.dto.QPostListRespDto;
import com.donation.domain.post.entity.PostState;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.donation.domain.post.entity.QPost.post;
import static com.donation.domain.post.entity.QPostDetailImage.postDetailImage;
import static com.donation.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostListRespDto> getPageDtoAll(Pageable pageable, PostState... postStates) {
        return queryFactory
                .select(new QPostListRespDto(
                        post.id.as("postId"),
                        user.id.as("userId"),
                        user.email,
                        user.name,
                        user.profileImage,
                        post.write,
                        post.amount,
                        post.currentAmount,
                        post.category,
                        post.state,
                        postDetailImage.imagePath.as("postMainImage")
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                       post.state.in(postStates)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<PostListRespDto> getUserIdPageDtoList(Long userId, Pageable pageable) {
        return queryFactory
                .select(new QPostListRespDto(
                        post.id.as("postId"),
                        user.id.as("userId"),
                        user.email,
                        user.name,
                        user.profileImage,
                        post.write,
                        post.amount,
                        post.currentAmount,
                        post.category,
                        post.state,
                        postDetailImage.imagePath.as("postMainImage")
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                        post.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
