package com.donation.domain.auth.repository;

import com.donation.global.exception.DonationNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenRepository implements TokenRepository{

    private static final Map<Long, String> TOKEN_REPOSITORY = new ConcurrentHashMap<>();

    @Override
    public String save(Long memberId, String refreshToken) {
        TOKEN_REPOSITORY.put(memberId, refreshToken);
        return TOKEN_REPOSITORY.get(memberId);
    }

    @Override
    public void deleteAll() {
        TOKEN_REPOSITORY.clear();
    }

    @Override
    public void deleteByMemberId(Long memberId) {
        TOKEN_REPOSITORY.remove(memberId);
    }

    @Override
    public boolean exist(Long memberId) {
        return TOKEN_REPOSITORY.containsKey(memberId);
    }

    @Override
    public String getToken(Long memberId) {
        Optional<String> token = Optional.ofNullable(TOKEN_REPOSITORY.get(memberId));
        return token.orElseThrow(() -> new DonationNotFoundException("일치하는 토큰이 존재하지 않습니다."));
    }
}
