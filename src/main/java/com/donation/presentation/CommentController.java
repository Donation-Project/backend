package com.donation.presentation;

import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.comment.dto.CommentSaveReqDto;
import com.donation.domain.comment.dto.CommentResponse;
import com.donation.domain.comment.application.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable(name = "id") Long postId,
                                        @RequestBody @Valid CommentSaveReqDto commentSaveReqDto,
                                        @LoginInfo LoginMember loginMember){
        commentService.saveComment(postId, loginMember, commentSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @PostMapping("/comment/{id}/reply")
    public ResponseEntity<?> addReply(@PathVariable(name = "id") Long postId,
                                        @RequestBody @Valid CommentSaveReqDto commentSaveReqDto,
                                        @LoginInfo LoginMember loginMember){
        commentService.saveReply(postId, loginMember, commentSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}/comment")
    public ResponseEntity<?> findComments(@PathVariable(name = "id") Long postId){
        List<CommentResponse> comment = commentService.findComment(postId);
        return ResponseEntity.ok(CommonResponse.success(comment));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id,
                                           @LoginInfo LoginMember loginMember){
        commentService.delete(id,loginMember);
        return ResponseEntity.ok(CommonResponse.success());
    }

}
