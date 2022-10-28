package com.donation.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;


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
    private final ImageCreator imageCreator;

    public String upload(final String encodedBase64Image) {
        PutObjectRequest putObjectRequest = imageCreator.toUploadData(bucket, encodedBase64ImageToByte(encodedBase64Image));
        amazonS3.putObject(putObjectRequest);
        return amazonS3.getUrl(bucket, putObjectRequest.getKey()).toString();
    }

    public byte[] encodedBase64ImageToByte(String encodedBase64Image) {
        return Base64.getMimeDecoder().decode(encodedBase64Image.substring(encodedBase64Image.indexOf(",") + 1));
    }

    public void delete(final String imageUrl) {
        String[] strings = imageCreator.validateDelete(imageUrl);
        if (strings == null)
            return;
        amazonS3.deleteObject(bucket, strings[3]);
    }
}
