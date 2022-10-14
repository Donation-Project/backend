package com.donation.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.donation.config.ConstConfig;
import com.donation.exception.s3.EmptyFileException;
import com.donation.exception.s3.FileUploadFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


/**
 * @description AWS S3 이미지 서비스
 * @author  정우진
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
            amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new EmptyFileException();
        }
    }

    public void delete(String imageUrl) {
        if (config.getBasicImageProfile().equals(imageUrl) || imageUrl.isEmpty())
            return;
        amazonS3.deleteObject(bucket, imageUrl);
    }
}
