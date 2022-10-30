package com.donation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.FavoriteFixtures.createFavorites;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FavoritesTest {

    @Test
    @DisplayName("좋아요 엔티티 생성 성공")
    void 좋아요_엔티티_생성_성공(){
        assertDoesNotThrow(() -> createFavorites(createUser(), createPost()));
    }
}
