package com.donation.service.s3;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.StringUtils.hasText;

@SpringBootTest
class AwsS3ServiceTest {

    @Autowired
    private AwsS3Service awsS3Service;

    @Test
    @DisplayName("S3(서비스) : MultiPartFile 업로드")
    void multiPartFile_Upload(){
        //given
        MultipartFile file = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());

        //when
        String imagePath = awsS3Service.upload(file);

        //then
        assertThat(hasText(imagePath)).isTrue();

        //clear
        awsS3Service.delete(imagePath);
    }

    @Test
    @DisplayName("S3(서비스) : Base64 업로드")
    void Base64_Upload(){
        //given
        String image = "Base64ImagesEncoding";
        byte[] imageDecode = Base64.getMimeDecoder().decode(image.substring(image.indexOf(",") + 1));

        //when
        String imagePath = awsS3Service.upload(imageDecode);

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