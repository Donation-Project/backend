package com.donation.domain.entites;

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
public class Donation extends BaseEntity{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "donationId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    @Builder
    public Donation(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }
}
