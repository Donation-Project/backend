package com.donation.service.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.donation.config.ConstConfig;
import com.donation.exception.DonationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ByteImageCreator implements ImageCreator<byte[]>{

    private final ConstConfig config;

    @Override
    public PutObjectRequest toUploadData(String bucket, byte[] imageData) {
        validateFileExists(imageData);
        return new PutObjectRequest(bucket, getRandomName(), new ByteArrayInputStream(imageData), getObjectMetadata(imageData));
    }
    protected String getRandomName() {
        return UUID.randomUUID().toString();
    }

    protected void validateFileExists(byte[] imageData) {
        if (imageData == null) {
            throw new DonationNotFoundException("파일이 존재하지 않습니다.");
        }
    }

    protected ObjectMetadata getObjectMetadata(byte[] imageData) {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(imageData.length);
        objMeta.setContentType("image/png");
        objMeta.setCacheControl("public, max-age=31536000");
        return objMeta;
    }

    @Override
    public String[] validateDelete(String imageUrl) {
        if (config.getBasicImageProfile().equals(imageUrl) || imageUrl.isEmpty())
            return null;
        return imageUrl.split("/");
    }

}
