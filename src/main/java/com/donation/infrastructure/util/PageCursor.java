package com.donation.infrastructure.util;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageCursor<T>{
    private CursorRequest nextCursorRequest;
    private List<T> request;

    @Builder
    public PageCursor(CursorRequest nextCursorRequest, List<T> request) {
        this.nextCursorRequest = nextCursorRequest;
        this.request = request;
    }
}
