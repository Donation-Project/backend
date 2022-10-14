package com.donation.repository.post;

import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.common.response.post.QPostFindRespDto;
import com.donation.common.response.post.QPostRespDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.donation.domain.entites.QPost.post;
import static com.donation.domain.entites.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    static boolean hasNextPage(List<?> content, Pageable pageable) {
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }

    @Override
    public Slice<PostRespDto> findPageableAll(Pageable pageable) {
        List<PostRespDto> postRespDtos = queryFactory
                .select(new QPostRespDto(
                        post.user.id.as("userId"),
                        post.user.name,
                        post.write,
                        post.amount,
                        post.category,
                        post.state,
                        post.user.createAt
                )).from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = hasNextPage(postRespDtos, pageable);

        return new SliceImpl<>(postRespDtos, pageable, hasNext);
    }

    @Override
    public Optional<PostFindRespDto> findByUserIdOrRoleAdmin(Long postId, Long userId) {
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
                .where(post.id.eq(postId), post.user.id.eq(userId))
                .stream().findAny();
    }
}
