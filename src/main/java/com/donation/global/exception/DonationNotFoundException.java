package com.donation.global.exception;

import org.springframework.http.HttpStatus;

public class DonationNotFoundException extends DonationException{

    public DonationNotFoundException(final String message) {
        super(message);
    }
    @Override
    public HttpStatus statusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
