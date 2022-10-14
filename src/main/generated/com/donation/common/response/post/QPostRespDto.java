package com.donation.common.response.post;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.donation.common.response.post.QPostRespDto is a Querydsl Projection type for PostRespDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostRespDto extends ConstructorExpression<PostRespDto> {

    private static final long serialVersionUID = 2096889146L;

    public QPostRespDto(com.querydsl.core.types.Expression<Long> userid, com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<? extends com.donation.domain.embed.Write> write, com.querydsl.core.types.Expression<Integer> amount, com.querydsl.core.types.Expression<com.donation.domain.enums.Category> category, com.querydsl.core.types.Expression<com.donation.domain.enums.PostState> state, com.querydsl.core.types.Expression<java.time.LocalDateTime> createAt) {
        super(PostRespDto.class, new Class<?>[]{long.class, String.class, com.donation.domain.embed.Write.class, int.class, com.donation.domain.enums.Category.class, com.donation.domain.enums.PostState.class, java.time.LocalDateTime.class}, userid, username, write, amount, category, state, createAt);
    }

}

