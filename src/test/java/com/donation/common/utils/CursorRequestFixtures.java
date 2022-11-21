package com.donation.common.utils;

import com.donation.infrastructure.util.CursorRequest;

public class CursorRequestFixtures {

    public static CursorRequest createCursor(){
        return new CursorRequest(null, null);
    }

    public static CursorRequest createCursor(Long key){
        return new CursorRequest(key, null);
    }
}
