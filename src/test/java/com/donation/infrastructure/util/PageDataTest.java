package com.donation.infrastructure.util;

import com.donation.global.exception.DonationNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PageDataTest {

    @Test
    @DisplayName("전체 페이지가 0일 경우에는 오류를 발생시킨다.")
    void 전체_페이지가_0일_경우에는_오류를_발생시킨다(){
        //given
        int totalPage = 0;

        //when & then
        Assertions.assertThatThrownBy(() -> new PageData(0, 0, totalPage, 0, true, true))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("해당 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("전체페이지보다 현재 페이지가 클 경우 오류를 발생시킨다")
    void test(){
        //given
        int totalPage = 1;
        int pageNum = 2;

        //when & then
        Assertions.assertThatThrownBy(() -> new PageData(pageNum, 0, totalPage, 0, true, true))
                .isInstanceOf(DonationNotFoundException.class)
                .hasMessage("존재하지 않는 페이지입니다.");
    }
}
