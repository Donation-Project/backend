package com.donation.domain.comment.entity;

import com.donation.infrastructure.embed.BaseEntity;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.global.exception.DonationInvalidateException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "comment")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = ALL)
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Embedded
    private Message message;

    private boolean softRemoved;

    @Builder
    public Comment(User user, Post post, String message, Comment parent) {
        this.user = user;
        this.post = post;
        this.message = new Message(message);
        this.parent = parent;
    }

    public static Comment parent(User user, Post post, String message) {
        return new Comment(user, post, message, null);
    }

    public static Comment child(User user, Post post, String message, Comment parent) {
        Comment child = new Comment(user, post, message, parent);
        parent.getChildren().add(child);
        return child;
    }

    public void validateOwner(Long useId) {
        if (!useId.equals(user.getId())) {
            throw new DonationInvalidateException("댓글의 작성자만 삭제할 수 있습니다.");
        }
    }

    public void deleteChild(Comment reply) {
        children.remove(reply);
    }

    public boolean isParent() {
        return Objects.isNull(parent);
    }

    public boolean hasNoReply() {
        return children.isEmpty();
    }

    public void changePretendingToBeRemoved() {
        this.softRemoved = true;
    }
}
