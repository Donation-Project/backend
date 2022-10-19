package com.donation.repository.donation;

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
public class DonationRepositoryCustomImpl implements DonationRepositoryCustom {

}
