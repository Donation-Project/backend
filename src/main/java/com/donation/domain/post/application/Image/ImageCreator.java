package com.donation.domain.post.application.Image;

import com.amazonaws.services.s3.model.PutObjectRequest;

public interface ImageCreator<T> {
    PutObjectRequest toUploadData(final String bucket, T imageData);
    String[] validateDelete(final String imageUrl);
}
