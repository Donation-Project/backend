package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.PostRepository;
import com.donation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    /**
     * save : 포스트등록
     */
    public PostRespDto saveV1(PostSaveReqDto postSaveReqDto, Long userid){
        User user = findUser(userid);
        postValidation(postSaveReqDto.getTitle(), userid);

        postRepository.save(postSaveReqDto.toPost(postSaveReqDto,user));
        return PostRespDto.toDto(postSaveReqDto.toPost(postSaveReqDto,user));
    }
    /**
     * update : 포스트수정
     */
    public PostRespDto updateV1(PostUpdateReqDto postUpdateReqDto, Long postid){
        Post post = postRepository.findById(postid)
                .orElseThrow(IllegalArgumentException::new);
        log.info("test{} {} {}",post.getUser().getName(),post.getUser().getId(),postid);
        Long userid = findUserId(post);
        postValidation(postUpdateReqDto.getTitle(),userid);

        return PostRespDto.toDto(post.update(postUpdateReqDto));
    }
    /**
     * delete : 포스트삭제
     */

    public void deleteV1(Long postid) {
        postRepository.findById(postid)
                .orElseThrow(IllegalArgumentException::new);
        postRepository.deleteById(postid);
    }
    /**
     * find : 포스트찾기
     */
    public PostRespDto findById(Long postid) {
        Post post = postRepository.findById(postid)
                .orElseThrow(IllegalArgumentException::new);
        return PostRespDto.toDto(post);
    }

    public Slice<PostRespDto> getList(Pageable pageable) {
        return postRepository.findPageableAll(pageable);
    }
    /**
     * Validation : 포스트검증
     */
    private void postValidation(String title, Long userid) {
        if(postRepository.findByUserIdAndWriteTitle(userid, title).isPresent())
            throw new IllegalArgumentException("유저가 동일한 제목을 입력했습니다");
    }

    /**
     *  값찾기
     */
    private static Long findUserId(Post post) {
        return post.getUser().getId();
    }
    private User findUser(Long userid) {
        return userRepository.findById(userid)
                .orElseThrow(IllegalArgumentException::new);
    }



}
