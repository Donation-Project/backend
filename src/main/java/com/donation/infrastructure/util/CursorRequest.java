package com.donation.infrastructure.util;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class CursorRequest {
    public static final Long NONE_KEY = -1L;

    @Nullable
    private Long key;

    private Integer size;

    public CursorRequest(Long key, Integer size) {
        this.key = key;
        this.size = size == null? 20 : size;
    }

    public Boolean hasKey(){
        return key != null;
    }

    public CursorRequest next(final Long key){
        return new CursorRequest(key, size);
    }
}
