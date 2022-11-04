package com.donation.service.user.auth.repository;

public interface TokenRepository {

    String save(final Long memberId, final String refreshToken);

    void deleteAll();

    void deleteByMemberId(final Long memberId);

    boolean exist(final Long memberId);

    String getToken(final Long memberId);
}
