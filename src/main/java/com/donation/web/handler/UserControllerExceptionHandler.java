package com.donation.web.handler;

import com.donation.common.CommonResponse;
import com.donation.common.ErrorResponse;
import com.donation.exception.HighestLevelException;
import com.donation.web.controller.user.UserController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerExceptionHandler {

    @ExceptionHandler(HighestLevelException.class)
    public ResponseEntity<?> bookSaveException(HighestLevelException e){
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(e.statusCode())
                .errorMessage(e.getMessage()).build()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        Map<String, String> validation = e.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode("400")
                .errorMessage(validation.toString()).build()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentExceptionHandler(IllegalArgumentException e){
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode("404")
                .errorMessage(e.getMessage()).build()),HttpStatus.OK);
    }
}
