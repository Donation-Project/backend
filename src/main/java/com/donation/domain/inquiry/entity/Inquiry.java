package com.donation.domain.inquiry.entity;

import com.donation.infrastructure.embed.Write;
import com.donation.infrastructure.embed.BaseEntity;
import com.donation.domain.user.entity.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "inquiry")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Inquiry extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "inquiry_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Write write;

    @Enumerated(STRING)
    private InquiryState state;

    @Builder
    public Inquiry(Long id, User user, Write write, InquiryState state) {
        this.id = id;
        this.user = user;
        this.write = write;
        this.state = state;
    }
}
