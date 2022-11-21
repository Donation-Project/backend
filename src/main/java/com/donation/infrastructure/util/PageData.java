package com.donation.infrastructure.util;

import com.donation.global.exception.DonationNotFoundException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageData {
    private int pageNum;      // 현제 페이지 번호
    private int pageElement;  // 현제 페이지의 요소 갯수
    private int totalPages;   // 전체 페이지 수
    private long totalElement; // 전체 요소 갯수
    private boolean first;
    private boolean last;

    @Builder
    public PageData(int pageNum, int pageElement, int totalPages, long totalElement, boolean first, boolean last) {
        this.pageNum = pageNum;
        this.pageElement = pageElement;
        this.totalPages = totalPages;
        this.totalElement = totalElement;
        this.first = first;
        this.last = last;

        validatePage(pageNum, totalPages);
    }
    private void validatePage(int pageNum, long totalPages){
        if (totalPages == 0){
            throw new DonationNotFoundException("해당 정보가 존재하지 않습니다.");
        }

        if(pageNum > totalPages){
            throw new DonationNotFoundException("존재하지 않는 페이지입니다.");
        }
    }
}
