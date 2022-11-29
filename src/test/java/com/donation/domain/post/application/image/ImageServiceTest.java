package com.donation.domain.post.application.image;

import com.donation.global.exception.DonationNotFoundException;
import com.donation.domain.post.application.Image.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.donation.common.UserFixtures.새로운_이미지;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.util.StringUtils.hasText;

@SpringBootTest
class ImageServiceTest {

    @Autowired
    private ImageService imageService;

    @Test
    @DisplayName("Base64로 인코딩된 이미지 업로드 성공")
    void Base64로_인코딩된_이미지_업로드_성공(){
        //given
        String image = 새로운_이미지;

        //when
        String imagePath = imageService.upload(image);

        //then
        assertThat(hasText(imagePath)).isTrue();

        //clear
        imageService.delete(imagePath);
    }

    @Test
    @DisplayName("Base64로 인코딩된 이미지가 없을 경우 예외를 던진다.")
    void Base64로_인코딩된_이미지가_없을_경우_예외를_던진다(){
        //given
        String image = "";

        //when & then
        assertThatThrownBy(() -> imageService.upload(image))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("파일이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이미지 경로가 비어있으면 삭제로직을 실행하지 않는다.")
    void 이미지_경로가_비어있으면_삭제로직을_실행하지_않는다(){
        //given
        String imagePath = "";

        //when
        Assertions.assertDoesNotThrow(() -> imageService.delete(imagePath));
    }
}