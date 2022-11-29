package com.donation.global;

import com.donation.infrastructure.common.CommonResponse;
import com.donation.infrastructure.common.ErrorResponse;
import com.donation.global.exception.DonationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class DonationAdvice {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> unexpectedException(final Exception e) {
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(BAD_REQUEST)
                .errorMessage(e.getMessage()).build()),BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> unexpectedRuntimeException(final RuntimeException e) {
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(BAD_REQUEST)
                .errorMessage(e.getMessage()).build()), BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        Map<String, String> validation = e.getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(BAD_REQUEST)
                .errorMessage(validation.toString()).build()), BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentExceptionHandler(IllegalArgumentException e){
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(BAD_REQUEST)
                .errorMessage(e.getMessage()).build()), BAD_REQUEST);
    }

    @ExceptionHandler(DonationException.class)
    public ResponseEntity<?> bookSaveException(DonationException e){
        return new ResponseEntity<>(CommonResponse.fail(ErrorResponse.builder().errorCode(e.statusCode())
                .errorMessage(e.getMessage()).build()), BAD_REQUEST);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class ExceptionDto {

        private String message;
    }
}
