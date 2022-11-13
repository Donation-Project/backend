package com.donation.domain.donation.repository;

import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.QDonationFindByFilterRespDto;
import com.donation.domain.post.entity.Category;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.donation.domain.donation.entity.QDonation.donation;
import static com.donation.domain.post.entity.QPost.post;

@RequiredArgsConstructor
public class DonationRepositoryCustomImpl implements DonationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DonationFindByFilterRespDto> findAllByFilter(DonationFilterReqDto donationFilterReqDto) {
        return queryFactory
                .select(new QDonationFindByFilterRespDto(
                                donation.id,
                                post.id,
                                donation.user.id,
                                post.write.title,
                                donation.amount,
                                post.currentAmount,
                                donation.user.name,
                                donation.post.user.name,
                                post.category
                        )
                )
                .from(donation)
                .leftJoin(donation.post, post)
                .where(
                        categoryEq(donationFilterReqDto.getCategory()),
                        usernameEq(donationFilterReqDto.getName())
                )
                .fetch();
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
