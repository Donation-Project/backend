package com.donation.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.donation.config.ConstConfig;
import com.donation.exception.DonationIOException;
import com.donation.exception.DonationNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * @author 정우진
 * @description AWS S3 이미지 서비스
 */
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final ConstConfig config;

    public String upload(MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
        ObjectMetadata objMeta = new ObjectMetadata();
        try {
            objMeta.setContentLength(multipartFile.getInputStream().available());
            objMeta.setContentType(multipartFile.getContentType());
            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        } catch (IOException e) {
            throw new DonationIOException(String.format("업로드를 실패했습니다.(%s)"));
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }


    public String upload(byte[] image) {
        ObjectMetadata objMeta = new ObjectMetadata();
        String s3FileName = UUID.randomUUID().toString();

        try {
            objMeta.setContentLength(image.length);
            objMeta.setContentType("image/png");
            objMeta.setCacheControl("public, max-age=31536000");
            amazonS3.putObject(new PutObjectRequest(bucket, s3FileName, new ByteArrayInputStream(image), objMeta));
        } catch (Exception e) {
            throw new DonationIOException(String.format("업로드를 실패했습니다.(%s)"));
        }
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }


    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new DonationNotFoundException("파일이 존재하지 않습니다.");
        }
    }

    public void delete(String imageUrl) {
        if (config.getBasicImageProfile().equals(imageUrl) || imageUrl.isEmpty())
            return;
        String[] image = imageUrl.split("/");
        amazonS3.deleteObject(bucket, image[3]);
    }
}
