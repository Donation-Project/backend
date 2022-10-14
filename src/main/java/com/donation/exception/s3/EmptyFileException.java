package com.donation.exception.s3;

import com.donation.exception.HighestLevelException;

public class EmptyFileException extends HighestLevelException {

    private static final String Message = "파일이 존재하지 않습니다.";

    public EmptyFileException(){
        super(Message);
    }


    @Override
    public String statusCode() {
        return "404";
    }
}
