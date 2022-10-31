package com.donation.common;

import com.donation.domain.entites.Comment;

import static com.donation.common.PostFixtures.*;
import static com.donation.common.UserFixtures.*;

public class CommentFixtures {
    public static String 일반_댓글 = "일반 댓글 입니다.";
    public static String 일반_대댓글 = "일반 대댓글 입니다.";

    public static Comment createParentComment(){
        return Comment.parent(createUser(1L), createPost(), 일반_댓글);
    }

    public static Comment createChildComment(Comment parent){
        return Comment.child(createUser(2L), createPost(),일반_대댓글, parent);
    }
}
