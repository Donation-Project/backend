package com.donation.exception.user;

import com.donation.exception.HighestLevelException;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends HighestLevelException {
    private static final String Message = "중복된 이메일이 있습니다";

    public EmailDuplicateException(){
        super(Message);
    }
    @Override
    public String statusCode() {
        return "400";
    }
}
