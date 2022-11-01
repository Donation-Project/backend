package com.donation.web.controller.comment;

import com.donation.common.CommonResponse;
import com.donation.common.request.comment.CommentSaveReqDto;
import com.donation.common.response.comment.CommentResponse;
import com.donation.service.comment.CommentService;
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

    @PostMapping("/post/{id}/comment/{userId}")
    public ResponseEntity<?> addComment(@PathVariable(name = "id") Long postId,
                                        @RequestBody @Valid CommentSaveReqDto commentSaveReqDto,
                                        @PathVariable Long userId){
        Long commentId = commentService.saveComment(postId, userId, commentSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(commentId), HttpStatus.CREATED);
    }

    @PostMapping("/comment/{id}/reply/{userId}")
    public ResponseEntity<?> addReply(@PathVariable(name = "id") Long postId,
                                        @RequestBody @Valid CommentSaveReqDto commentSaveReqDto,
                                        @PathVariable Long userId){
        Long commentId = commentService.saveReply(postId, userId, commentSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(commentId), HttpStatus.CREATED);
    }

    @GetMapping("/post/{id}/comment")
    public ResponseEntity<?> findComments(@PathVariable(name = "id") Long postId){
        List<CommentResponse> comment = commentService.findComment(postId);
        return new ResponseEntity<>(CommonResponse.success(comment), HttpStatus.CREATED);
    }

    @DeleteMapping("/comment/{id}/{userId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long userId){
        commentService.delete(id,userId);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

}
