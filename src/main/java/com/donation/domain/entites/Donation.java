package com.donation.domain.entites;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Donation extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "donation_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
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
