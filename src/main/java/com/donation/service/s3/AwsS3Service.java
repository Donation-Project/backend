package com.donation.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;
    private final ImageCreator imageCreator;

    public String upload(final String encodedBase64Image) {

        PutObjectRequest putObjectRequest = imageCreator.toUploadData(bucket, encodedBase64Image);
        amazonS3.putObject(putObjectRequest);
        return amazonS3.getUrl(bucket, putObjectRequest.getKey()).toString();
    }


    public void delete(final String imageUrl) {
        String[] imagePath = imageCreator.validateDelete(imageUrl);
        if(imagePath != null){
            amazonS3.deleteObject(bucket, imagePath[3]);
        }
    }
}
