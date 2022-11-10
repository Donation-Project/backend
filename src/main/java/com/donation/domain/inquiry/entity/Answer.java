package com.donation.domain.inquiry.entity;

import com.donation.infrastructure.embed.Write;
import com.donation.infrastructure.embed.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "answer")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Answer extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    private Write write;

    @Builder
    public Answer(Long id, Inquiry inquiry, Write write) {
        this.id = id;
        this.inquiry = inquiry;
        this.write = write;
    }
}
