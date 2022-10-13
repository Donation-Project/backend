package com.donation.exception.s3;

import com.donation.exception.HighestLevelException;

public class FileUploadFailedException extends HighestLevelException {

    private static final String Message = "파일 업로드에 실패했습니다.";

    public FileUploadFailedException(){
        super(Message);
    }

    @Override
    public String statusCode() {
        return "500";
    }
}
