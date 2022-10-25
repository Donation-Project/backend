package com.donation.exception;

import org.springframework.http.HttpStatus;


public abstract class DonationException extends RuntimeException{


    public DonationException(String message) {
        super(message);
    }

    public abstract HttpStatus statusCode();
}
