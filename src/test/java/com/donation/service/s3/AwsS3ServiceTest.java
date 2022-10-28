package com.donation.service.s3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StringUtils.hasText;

@SpringBootTest
class AwsS3ServiceTest {

    @Autowired
    private AwsS3Service awsS3Service;

    @Test
    @DisplayName("S3(서비스) : Base64 업로드")
    void Base64_Upload(){
        //given
        String image = "Base64ImagesEncoding";

        //when
        String imagePath = awsS3Service.upload(image);

        //then
        assertThat(hasText(imagePath)).isTrue();

        //clear
        awsS3Service.delete(imagePath);
    }

    @Test
    @DisplayName("S3(서비스) : 이미지 경로가 비어있으면 삭제로직을 실행하지 않음")
    void nonDelete(){
        //given
        String imagePath = "";

        //when
        awsS3Service.delete(imagePath);
    }
}