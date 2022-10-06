package com.donation.web.controller;

import com.donation.common.CommonResponse;
import com.donation.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> bookSaveException(IllegalArgumentException e){
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(400)
                .errorMessage(e.getMessage()).build()), HttpStatus.BAD_REQUEST);
    }

}
