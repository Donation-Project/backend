package com.donation.exception;

public class NoSuchElementException extends HighestLevelException  {

    private static final String Message = "존재하지 않는 정보입니다.";

    public NoSuchElementException(){
        super(Message);
    }

    @Override
    public String statusCode() {
        return "400";
    }
}
