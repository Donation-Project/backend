//package com.donation.service.favorite;
//
//import com.donation.common.response.user.UserRespDto;
//import com.donation.domain.entites.Favorites;
//import com.donation.domain.entites.Post;
//import com.donation.domain.entites.User;
//import com.donation.repository.post.PostRepository;
//import com.donation.repository.user.UserRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class FavoriteServiceTest {
//    @Autowired
//    private FavoriteService favoriteService;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PostRepository postRepository;
//
//    Favorites getFavorite(){
//        User user = User.builder().build();
//        userRepository.save(user);
//
//        Post post = Post.builder()
//                .user(user)
//                .build();
//        postRepository.save(post);
//
//        return Favorites.builder()
//                .user(user)
//                .post(post)
//                .build();
//    }
//
//    @AfterEach
//    void clear(){
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//    }
//
//    @Test
//    @DisplayName("좋아요(서비스) : 저장")
//    void save(){
//        //given
//        Favorites favorite = getFavorite();
//        Long postId = favorite.getPost().getId();
//        Long userId = favorite.getUser().getId();
//
//        //when
//        favoriteService.saveAndCancel(postId, userId);
//
//        //then
//        Boolean isPresent = favoriteService.findById(postId, userId);
//        assertThat(isPresent).isTrue();
//
//        //clear
//        favoriteService.deletePostId(postId);
//    }
//
//    @Test
//    @DisplayName("좋아요(서비스) : 취소")
//    void cancel(){
//        //when
//        Favorites favorite = getFavorite();
//        Long postId = favorite.getPost().getId();
//        Long userId = favorite.getUser().getId();
//
//        //when
//        favoriteService.saveAndCancel(postId, userId);
//        favoriteService.saveAndCancel(postId, userId);
//
//        //then
//        Boolean isPresent = favoriteService.findById(postId, userId);
//        assertThat(isPresent).isFalse();
//
//        //clear
//        favoriteService.deletePostId(postId);
//    }
//
//
//    @Test
//    @DisplayName("좋아요(서비스) : 리스트 조회")
//    void getList() {
//        //given
//        List<User> users = IntStream.range(1, 31)
//                .mapToObj(i -> User.builder()
//                        .username("username" + i + "@naver.com")
//                        .name("name" + i)
//                        .password("password" + i)
//                        .build()
//                )
//                .collect(Collectors.toList());
//        userRepository.saveAll(users);
//        Post post = postRepository.save(Post.builder()
//                .user(users.get(0))
//                .build());
//        users.stream()
//                .forEach(u-> {
//                    favoriteService.saveAndCancel(post.getId(), u.getId());
//                });
//
//        //when
//        List<UserRespDto> result = favoriteService.findAll(post.getId());
//
//        //then
//        assertThat(result.size()).isEqualTo(30);
//        assertThat(result.get(0).getId()).isEqualTo(users.get(0).getId());
//        assertThat(result.get(29).getId()).isEqualTo(users.get(29).getId());
//
//        //clear
//        favoriteService.deletePostId(post.getId());
//    }
//
//    @Test
//    @DisplayName("좋아요(서비스) : count 조회")
//    void count() {
//        //given
//        List<User> users = IntStream.range(1, 31)
//                .mapToObj(i -> User.builder()
//                        .username("username" + i + "@naver.com")
//                        .name("name" + i)
//                        .password("password" + i)
//                        .build()
//                )
//                .collect(Collectors.toList());
//        userRepository.saveAll(users);
//        Post post = postRepository.save(Post.builder()
//                .user(users.get(0))
//                .build());
//        users.stream()
//                .forEach(u-> {
//                    favoriteService.saveAndCancel(post.getId(), u.getId());
//                });
//
//        //when
//        Long count =  favoriteService.count(post.getId());
//
//        //then
//        assertThat(count).isEqualTo(30);
//
//        //clear
//        favoriteService.deletePostId(post.getId());
//    }
//
//
//    @Test
//    @DisplayName("좋아요(서비스) : 포스팅 전체 좋아요 삭제")
//    void delete(){
//        //given
//        List<User> users = IntStream.range(1, 31)
//                .mapToObj(i -> User.builder()
//                        .username("username" + i + "@naver.com")
//                        .name("name" + i)
//                        .password("password" + i)
//                        .build()
//                )
//                .collect(Collectors.toList());
//        userRepository.saveAll(users);
//        Post post = postRepository.save(Post.builder()
//                .user(users.get(0))
//                .build());
//        users.stream()
//                .forEach(u-> {
//                    favoriteService.saveAndCancel(post.getId(), u.getId());
//                });
//
//        //when
//        favoriteService.deletePostId(post.getId());
//
//        //then
//        assertThat(favoriteService.findAll(post.getId())).isEmpty();
//    }
//}