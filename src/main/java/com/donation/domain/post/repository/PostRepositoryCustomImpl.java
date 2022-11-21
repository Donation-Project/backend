package com.donation.domain.post.repository;

import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.dto.QPostListRespDto;
import com.donation.domain.post.entity.PostState;
import com.donation.infrastructure.util.CursorRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.donation.domain.post.entity.QPost.post;
import static com.donation.domain.post.entity.QPostDetailImage.postDetailImage;
import static com.donation.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostListRespDto> findDtoAllByIdLessThanAndStateInOrderByIdDesc(CursorRequest cursorRequest, PostState... states) {
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
                        postDetailImage.imagePath
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                       post.state.in(states),
                        idLt(cursorRequest.getKey())
                )
                .orderBy(post.id.desc())
                .limit(cursorRequest.getSize())
                .fetch();
    }



    @Override
    public List<PostListRespDto> findDtoAllByUserIdOrderByIdDesc(Long userId, CursorRequest cursorRequest) {
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
                        postDetailImage.imagePath
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                        post.user.id.eq(userId),
                        idLt(cursorRequest.getKey())
                )
                .orderBy(post.id.desc())
                .limit(cursorRequest.getSize())
                .fetch();
    }

    private BooleanExpression idLt(Long id){
        return id != null ? post.id.lt(id) : null;
    }
}
