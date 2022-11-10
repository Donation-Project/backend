package com.donation.infrastructure.Image;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.donation.global.exception.DonationInvalidateException;
import com.donation.global.exception.DonationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ByteImageCreator implements ImageCreator{

    @Value("${profileImageUrl}")
    private String profileImageUrl;

    @Override
    public PutObjectRequest toUploadData(String bucket, Object imageData) {
        byte[] image = validateFileExists((String) imageData);
        return new PutObjectRequest(bucket, getRandomName(), new ByteArrayInputStream(image), getObjectMetadata(image));
    }
    private String getRandomName() {
        return UUID.randomUUID().toString();
    }

    protected byte[] validateFileExists(String encodedBase64Image) {
        if (!StringUtils.hasText(encodedBase64Image)) {
            throw new DonationNotFoundException("파일이 존재하지 않습니다.");
        }
        return encodedBase64ImageToByte(encodedBase64Image);
    }

    private byte[] encodedBase64ImageToByte(String encodedBase64Image) {
        return Optional.of(Base64.getMimeDecoder().decode(encodedBase64Image.substring(encodedBase64Image.indexOf(",") + 1)))
                .orElseThrow(() -> new DonationInvalidateException("손상되거나 잘못된 이미지 요청입니다."));
    }

    private ObjectMetadata getObjectMetadata(byte[] imageData) {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(imageData.length);
        objMeta.setContentType("image/png");
        objMeta.setCacheControl("public, max-age=31536000");
        return objMeta;
    }


    @Override
    public String[] validateDelete(String imageUrl) {
        if (profileImageUrl.equals(imageUrl) || imageUrl.isEmpty())
            return null;
        return imageUrl.split("/");
    }

}
