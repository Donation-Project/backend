package com.donation.repository.donation;

import com.donation.domain.entites.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<Donation, Long>, DonationRepositoryCustom  {

}
