package com.donation.domain.donation.repository;

import com.donation.domain.donation.entity.Donation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom  {

    @EntityGraph(attributePaths = {"post"})
    List<Donation> findAllByUserId(Long userid);
}
