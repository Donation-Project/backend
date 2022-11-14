package com.donation.domain.donation.entity;

import com.donation.global.exception.DonationInvalidateException;
import com.donation.infrastructure.embed.BaseEntity;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "donation")
@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Donation extends BaseEntity {
    public static final Float MAX_AMOUNT =1000000.0F;
    public static final Float MIN_AMOUNT =0.0F;

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "donationId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    private String amount;

    @Builder
    public Donation(Long id, User user, Post post,String amount) {
        String validateAmount = validate(amount);
        this.id = id;
        this.user = user;
        this.post = post;
        this.amount=amount;
    }

    public static Donation of(User user, Post post, String amount){
        String validateAmount = validate(amount);
        return Donation.builder()
                .user(user)
                .post(post)
                .amount(validateAmount)
                .build();
    }

    public static Float toFloat(String amount){
        return Float.parseFloat(amount);
    }
    public static String validate(String amount){
        Float Amount = toFloat(amount);
        if(Amount> MIN_AMOUNT &&Amount<= MAX_AMOUNT){
            return Amount.toString();
        }else {
            throw new DonationInvalidateException(String.format("후원금액은 %.1f 보다크고 %.1f보다 작아야합니다", MIN_AMOUNT, MAX_AMOUNT) );

        }
    }
}
