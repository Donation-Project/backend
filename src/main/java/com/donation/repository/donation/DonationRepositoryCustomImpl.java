package com.donation.repository.donation;

import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.common.response.donation.QDonationFindRespDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.donation.domain.entites.QDonation.donation;
import static com.donation.domain.entites.QPost.post;
import static com.donation.domain.entites.QPostDetailImage.postDetailImage;

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
                                                .where(donation.user.id.eq(id))
                                                .groupBy(donation.post.id),
                                        "total"),
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
}
