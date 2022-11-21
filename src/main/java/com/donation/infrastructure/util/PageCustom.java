package com.donation.infrastructure.util;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageCustom<T> {
    private PageData page;
    private List<T> content;
    public PageCustom(Page<T> page) {
        this.page = PageData.builder()
                .pageNum(page.getNumber() + 1)
                .pageElement(page.getNumberOfElements())
                .totalPages(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .first(page.isFirst())
                .last(page.isLast())
                .build();

        this.content = page.getContent();
    }
}
