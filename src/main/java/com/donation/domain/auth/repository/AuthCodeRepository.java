package com.donation.domain.auth.repository;

import com.donation.domain.auth.entity.AuthCode;
import com.donation.global.exception.DonationNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {

    void deleteByAuthSerialNumber(String authSerialNumber);

    Optional<AuthCode> findByAuthSerialNumber(String authSerialNumber);

    default AuthCode getByAuthSerialNumber(String authSerialNumber){
        return findByAuthSerialNumber(authSerialNumber)
                .orElseThrow(() -> new DonationNotFoundException("인증 정보가 존재하지 않습니다."));
    }

    boolean existsByAuthSerialNumber(String authSerialNumber);

    default void validateExistByAuthSerialNumber(String authSerialNumber){
        if (!existsByAuthSerialNumber(authSerialNumber))
            throw new DonationNotFoundException("발급된 인증코드가 존재하지 않습니다.");
    }
}
