package com.donation.repository.utils;

import org.springframework.data.domain.Pageable;

import java.util.List;

public class PagingUtils {

    public static boolean hasNextPage(List<?> content, Pageable pageable){
        if (content.size() > pageable.getPageSize()){
            content.remove(pageable.getPageSize());
            return true;
        }
        return false;
    }
}
