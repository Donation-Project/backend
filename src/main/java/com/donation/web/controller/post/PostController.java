package com.donation.web.controller.post;

import com.donation.common.CommonResponse;
import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/{id}")
    public ResponseEntity<?> savePostV1(@RequestBody @Valid PostSaveReqDto postSaveReqDto
            ,@PathVariable("id") Long userid){

        PostRespDto save = postService.saveV1(postSaveReqDto,userid);
        return new ResponseEntity<>(CommonResponse.success(save), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePostV1(@RequestBody @Valid PostUpdateReqDto postUpdateReqDto
            , @PathVariable("id") Long postid){
        PostRespDto update = postService.updateV1(postUpdateReqDto,postid);
        return new ResponseEntity<>(CommonResponse.success(update), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeletePostV1(@PathVariable("id") Long postid){
    postService.deleteV1(postid);
        return  ResponseEntity.ok(CommonResponse.success());
    }
    @GetMapping
    public ResponseEntity<?> getPostList(Pageable pageable){
        Slice<PostRespDto> list = postService.getList(pageable);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable("id") Long postid){
        PostRespDto post = postService.findById(postid);
        return new ResponseEntity<>(CommonResponse.success(post), HttpStatus.OK);
    }

}
