package com.donation.repository.donation;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.common.response.donation.QDonationFindByFilterRespDto;
import com.donation.common.response.donation.QDonationFindRespDto;
import com.donation.domain.enums.Category;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.donation.domain.entites.QDonation.donation;
import static com.donation.domain.entites.QPost.post;
import static com.donation.repository.utils.PagingUtils.hasNextPage;

@RequiredArgsConstructor
public class DonationRepositoryCustomImpl implements DonationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DonationFindRespDto> findAllByUserId(Long id) {
        return queryFactory
                .select(new QDonationFindRespDto(
                                post.write.title,
                                donation.amount,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(donation.amount.sum())
                                                .from(donation)
                                                .where(donation.user.id.eq(id)),
                                        "total"),
                                post.id,
                                post.category,
                                donation.createAt
                        )
                )
                .from(donation)
                .leftJoin(donation.post, post)
                .where(
                        donation.user.id.eq(id)
                )
                .fetch();
    }


    @Override
    public Slice<DonationFindByFilterRespDto> findAllByFilter(Pageable pageable, DonationFilterReqDto donationFilterReqDto) {
        List<DonationFindByFilterRespDto> content = queryFactory
                .select(new QDonationFindByFilterRespDto(
                                donation.id,
                                post.id,
                                post.write.title,
                                donation.amount,
                                donation.user.name,
                                donation.post.user.name,
                                donation.createAt,
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(donation.amount.sum())
                                                .from(donation),
                                        "total"),
                                post.category
                        )
                )
                .from(donation)
                .leftJoin(donation.post, post)
                .where(
                        categoryEq(donationFilterReqDto.getCategory()),
                        usernameEq(donationFilterReqDto.getUsername())
                ).offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = hasNextPage(content, pageable);
        return new SliceImpl<>(content, pageable, hasNext);
    }

    private static BooleanExpression usernameEq(String username) {
        if (username == null) {
            return null;
        }
        return donation.user.name.eq(username);
    }

    private static BooleanExpression categoryEq(Category category) {
        if (category == null) {
            return null;
        }
        return post.category.eq(category);
    }
}
