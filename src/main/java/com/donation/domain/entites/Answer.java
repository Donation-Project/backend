package com.donation.domain.entites;

import com.donation.domain.embed.Write;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class Answer extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answerId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inquirtId")
    private Inquiry inquiry;

    private Write title;

    @Builder
    public Answer(Long id, Inquiry inquiry, Write title) {
        this.id = id;
        this.inquiry = inquiry;
        this.title = title;
    }
}
