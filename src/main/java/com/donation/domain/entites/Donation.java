package com.donation.domain.entites;

import com.donation.common.response.donation.DonationFindRespDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Donation extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "donationId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    private float amount;

    @Builder
    public Donation(Long id, User user, Post post,float amount) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.amount=amount;
    }

}
