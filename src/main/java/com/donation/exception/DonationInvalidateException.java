package com.donation.exception;

import org.springframework.http.HttpStatus;

public class DonationInvalidateException extends DonationException{

    public DonationInvalidateException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus statusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
