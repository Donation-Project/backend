package com.donation.domain.entites;

import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.domain.embed.Write;
import com.donation.domain.enums.Category;
import com.donation.domain.enums.PostState;
import com.donation.exception.DonationNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name="post")
@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Embedded
    private Write write;

    private String amount;

    private float currentAmount;

    @Enumerated(STRING)
    private Category category;

    @Enumerated(STRING)
    private PostState state;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostDetailImage> postDetailImages = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Favorites> favorites = new HashSet<>();

    @Builder
    public Post(Long id, User user, Write write, String amount, Category category, PostState state) {
        this.id = id;
        this.user = user;
        this.write = write;
        this.amount = amount;
        this.currentAmount = 0;
        this.category = category;
        this.state = state;
    }

    public void addPostImage(PostDetailImage postDetailImage){
        postDetailImage.setPost(this);
        this.getPostDetailImages().add(postDetailImage);
    }

    public void addFavorite(Favorites favorites){
        favorites.setPost(this);
        this.getFavorites().add(favorites);
    }

    public Post changePost(PostUpdateReqDto dto) {
        this.write = dto.getWrite();
        this.category = dto.getCategory();
        this.amount = dto.getAmount();
        return this;
    }

    public Post confirm(PostState state) {
        this.state=state;
        return this;
    }

    public void decrease(final float amount){
        if(this.currentAmount + amount > Float.parseFloat(this.amount)){
            throw new DonationNotFoundException("목표금액보다 금액이 커질 수 없습니다.");
        }
        currentAmount += amount;
    }
}
