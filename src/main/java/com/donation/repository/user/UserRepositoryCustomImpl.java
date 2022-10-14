package com.donation.repository.user;

import com.donation.common.response.user.QUserRespDto;
import com.donation.common.response.user.UserRespDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.donation.domain.entites.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<UserRespDto> findPageableAll(Pageable pageable) {
        List<UserRespDto> userRespDtos = queryFactory
                .select(new QUserRespDto(
                        user.id.as("userId"),
                        user.username,
                        user.name,
                        user.profileImage
                )).from(user)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = hasNextPage(userRespDtos, pageable);

        return new SliceImpl<>(userRespDtos, pageable, hasNext);
    }

    static boolean hasNextPage(List<?> content, Pageable pageable){
        if (content.size() > pageable.getPageSize()){
            content.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }


}
