package com.donation.web.controller.post;

import com.donation.common.CommonResponse;
import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.service.post.PostService;
import com.donation.service.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donation.domain.enums.PostState.APPROVAL;
import static com.donation.domain.enums.PostState.COMPLETION;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/{id}")
    public ResponseEntity<?> save(
            @PathVariable Long id,
            @RequestBody @Valid PostSaveReqDto postSaveReqDto
    ){
        PostSaveRespDto post = postService.save(postSaveReqDto, id);
        return new ResponseEntity<>(CommonResponse.success(post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        PostFindRespDto post = postService.findById(id);
        return ResponseEntity.ok(CommonResponse.success(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestBody @Valid PostUpdateReqDto postUpdateReqDto,
            @PathVariable Long id){
        postService.update(postUpdateReqDto, id);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        postService.delete(id);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping
    public ResponseEntity<?> getPostList(Pageable pageable){
        Slice<PostListRespDto> list = postService.getList(pageable, APPROVAL, COMPLETION);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
}
