package com.donation.repository.post;

import com.donation.common.response.post.*;
import com.donation.domain.enums.PostState;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.donation.domain.entites.QPost.post;
import static com.donation.domain.entites.QPostDetailImage.postDetailImage;
import static com.donation.domain.entites.QUser.user;
import static com.donation.domain.enums.PostState.DELETE;
import static com.donation.domain.enums.PostState.WAITING;
import static com.donation.repository.utils.PagingUtils.hasNextPage;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostFindRespDto> findDetailPostById(Long postId) {
        return queryFactory
                .select(new QPostFindRespDto(
                        post.id.as("postId"),
                        user.id.as("userId"),
                        user.username.as("username"),
                        user.name.as("name"),
                        user.profileImage.as("profileImage"),
                        post.write.as("write"),
                        post.amount,
                        post.category.as("category"),
                        post.state.as("postState")
                ))
                .from(post)
                .leftJoin(post.user, user)
                .where(
                        post.id.eq(postId)
                )
                .stream().findAny();
    }



    @Override
    public Slice<PostListRespDto> findDetailPostAll(Pageable pageable) {
        List<PostListRespDto> content = queryFactory
                .select(new QPostListRespDto(
                        post.id.as("postId"),
                        user.id.as("userId"),
                        user.username.as("username"),
                        user.name.as("name"),
                        user.profileImage.as("profileImage"),
                        post.write.as("write"),
                        post.amount,
                        post.category.as("category"),
                        post.state.as("postState"),
                        postDetailImage.imagePath.as("postMainImage")
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                        postStateNe(WAITING),
                        postStateNe(DELETE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = hasNextPage(content, pageable);

        return new SliceImpl<>(content, pageable, hasNext);
    }

    @Override
    public Slice<PostListRespDto> findAllUserId(Long userId, Pageable pageable) {
        List<PostListRespDto> content = queryFactory
                .select(new QPostListRespDto(
                        post.id.as("postId"),
                        user.id.as("userId"),
                        user.username.as("username"),
                        user.name.as("name"),
                        user.profileImage.as("profileImage"),
                        post.write.as("write"),
                        post.amount,
                        post.category.as("category"),
                        post.state.as("postState"),
                        postDetailImage.imagePath.as("postMainImage")
                ))
                .from(post)
                .leftJoin(post.user, user)
                .leftJoin(post.postDetailImages, postDetailImage)
                .where(
                        post.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = hasNextPage(content, pageable);

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression postStateNe(PostState state){
        return state != null ? post.state.ne(state) : null;
    }
}
