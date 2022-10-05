package com.donation.domain.entites;

import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class PostWait extends BaseEntity{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Embedded
    private Write write;

    @Enumerated(STRING)
    private Category category;

    private Integer amount;


    @Builder
    public PostWait(Long id, User user, Write write, Integer amount) {
        this.id = id;
        this.user = user;
        this.write = write;
        this.amount = amount;
    }
}
