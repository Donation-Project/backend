package com.donation.domain.entites;

import com.donation.domain.embed.Write;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "reviews")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Reviews extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Write write;

    @Builder
    public Reviews(Long id, User user, Post post, Write write) {
        this.id = id;
        this.user = user;
        this.post = post;
        this.write = write;
    }
}
