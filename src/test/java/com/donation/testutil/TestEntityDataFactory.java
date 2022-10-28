package com.donation.testutil;

import com.donation.domain.embed.Write;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.domain.enums.PostState.APPROVAL;
import static com.donation.domain.enums.PostState.WAITING;

public class TestEntityDataFactory {

    static String imagePath = "https://localhost:8080/image";
    static String metamask = "https://metamasek.com";

    public static User createUser() {
        return User.builder()
                .username("username")
                .password("passowrd")
                .name("name")
                .role(Role.USER)
                .profileImage(imagePath)
                .provider("provider")
                .providerId("providerId")
                .metamask(metamask)
                .build();
    }

    public static User createUser(String username) {
        return User.builder()
                .username(username)
                .password("passowrd")
                .name("name")
                .role(Role.USER)
                .profileImage(imagePath)
                .provider("provider")
                .providerId("providerId")
                .metamask(metamask)
                .build();
    }

    public static User createUser(Long id) {
        return User.builder()
                .id(id)
                .username("username")
                .password("passowrd")
                .name("name")
                .role(Role.USER)
                .profileImage(imagePath)
                .provider("provider")
                .providerId("providerId")
                .metamask(metamask)
                .build();
    }

    public static Post createPost(){
        return Post.builder()
                .write(new Write("title", "content"))
                .amount("12")
                .state(APPROVAL)
                .category(ETC)
                .build();
    }

    public static Post createPost(User user){
        return Post.builder()
                .user(user)
                .write(new Write("title", "content"))
                .amount("12")
                .state(APPROVAL)
                .category(ETC)
                .build();
    }

    public static Post createPost(String title, String content){
        return Post.builder()
                .write(new Write(title, content))
                .amount("12")
                .state(WAITING)
                .category(ETC)
                .build();
    }

    public static Post createPost(User user, String title, String content){
        return Post.builder()
                .user(user)
                .write(new Write(title, content))
                .amount("12")
                .state(APPROVAL)
                .category(ETC)
                .build();
    }

    public static Favorites createFavorites(User user, Post post) {
        return Favorites.of(user, post);
    }

    public static Donation createDonation(User user, Post post, String amount){
        return  Donation.builder()
                .user(user)
                .post(post)
                .amount(amount)
                .build();
    }
}
