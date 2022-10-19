package com.donation.repository.post;

import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.QPostFindRespDto;
import com.donation.common.response.post.QPostListRespDto;
import com.donation.domain.enums.PostState;
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
                        user.username,
                        user.name,
                        user.profileImage,
                        post.write,
                        post.amount,
                        post.category,
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
    public Slice<PostListRespDto> findDetailPostAll(Pageable pageable, PostState... postStates) {
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
                       post.state.in(postStates)

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
}
