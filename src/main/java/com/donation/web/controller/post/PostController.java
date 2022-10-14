package com.donation.web.controller.post;

import com.donation.common.CommonResponse;
import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.common.response.post.PostSaveRespDto;
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
    public ResponseEntity<?> save(
            @RequestBody @Valid PostSaveReqDto postSaveReqDto,
            @PathVariable("id") Long userid){

        PostSaveRespDto post = postService.save(postSaveReqDto, userid);
        return new ResponseEntity<>(CommonResponse.success(post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOnePost(@PathVariable Long id, @RequestParam(required = false) Long userId){
        if (userId == null){
            return new ResponseEntity<>(CommonResponse.success(postService.findById(id)), HttpStatus.OK);
        }

        log.info("==================");

        PostFindRespDto post = postService.findById(id, userId);
        return new ResponseEntity<>(CommonResponse.success(post), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> contentUpdate(
            @RequestBody @Valid PostUpdateReqDto postUpdateReqDto,
            @PathVariable Long id){

        postService.contentUpdate(postUpdateReqDto, id);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.delete(id);
        return  ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping
    public ResponseEntity<?> getPostList(Pageable pageable){
        Slice<PostRespDto> list = postService.getList(pageable);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
}
