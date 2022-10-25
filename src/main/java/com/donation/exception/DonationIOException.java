package com.donation.exception;

import org.springframework.http.HttpStatus;

public class DonationIOException extends DonationException {
    public DonationIOException(String message) {
        super(message);
    }

    @Override
    public HttpStatus statusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
