package com.donation.domain.comment.entity;

import com.donation.domain.comment.entity.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.CommentFixtures.createChildComment;
import static com.donation.common.CommentFixtures.createParentComment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CommentTest {

    @Test
    @DisplayName("댓글이 정상적으로 작성된다.")
    void 댓글이_정상적으로_생성된다(){
        //given & when & then
        assertDoesNotThrow(() -> createParentComment());
    }

    @Test
    @DisplayName("대댓글이 정상적으로 작성된다.")
    void 대댓글이_정상적으로_생성된다(){
        //given
        Comment parent = createParentComment();

        //when & then
        assertDoesNotThrow(() -> createChildComment(parent));
    }

    @Test
    @DisplayName("댓글에 상위 댓글이 없으면 True를 반환한다.")
    void 댓글에_상위_댓글이_없으면_True를_반환한다(){
        //given
        Comment parent = createParentComment();

        //when & then
        assertThat(parent.isParent()).isTrue();
    }

    @Test
    @DisplayName("댓글에 달린 대댓글을 삭제한다")
    void 댓글에_달린_대댓글을_삭제한다(){
        //given
        Comment parent = createParentComment();
        Comment child = createChildComment(parent);

        //when
        parent.deleteChild(child);

        //then
        assertThat(parent.hasNoReply()).isTrue();
    }
}
