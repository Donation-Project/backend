package com.donation.global.exception;

import org.springframework.http.HttpStatus;

public class DonationDuplicateException extends DonationException {


    public DonationDuplicateException(final String message) {
        super(message);
    }

    @Override
    public HttpStatus statusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
