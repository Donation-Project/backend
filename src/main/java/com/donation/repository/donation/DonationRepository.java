package com.donation.repository.donation;

import com.donation.domain.entites.Donation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom  {

    @EntityGraph(attributePaths = {"post"})
    List<Donation> findAllByUserId(Long userid);
}
