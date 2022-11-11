package com.donation.domain.reviews.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.BaseEntity;
import com.donation.infrastructure.embed.Write;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "reviews")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Reviews extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = LAZY)
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

    public static Reviews of(User user, Post post, Write write){
        return Reviews.builder()
                .user(user)
                .post(post)
                .write(write)
                .build();
    }


    public void validateOwner(Long useId) {
        if (!useId.equals(user.getId())) {
            throw new DonationInvalidateException("작성자만 권한이 있습니다.");
        }
    }

    public void changeWrite(final Write write){
        this.write = write;
    }
}
