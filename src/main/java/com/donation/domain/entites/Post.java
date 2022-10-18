package com.donation.domain.entites;

import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_Id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Embedded
    private Write write;

    private int amount;

    @Enumerated(STRING)
    private Category category;

    @Enumerated(STRING)
    private PostState state;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostDetailImage> postDetailImages = new ArrayList<>();

    @Builder
    public Post(Long id, User user, Write write, int amount, Category category, PostState state, List<PostDetailImage> postDetailImages) {
        this.id = id;
        this.user = user;
        this.write = write;
        this.amount = amount;
        this.category = category;
        this.state = state;
        this.postDetailImages = postDetailImages;
    }


    public Post update(PostUpdateReqDto dto) {
        this.write = Write.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        this.category = dto.getCategory();
        this.amount = dto.getAmount();
        return this;
    }

    public Post confirm(PostState state) {
        this.state=state;
        return this;
    }
}
