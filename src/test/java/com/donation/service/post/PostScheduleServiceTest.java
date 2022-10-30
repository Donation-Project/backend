package com.donation.service.post;


import com.donation.common.utils.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PostScheduleServiceTest extends ServiceTest {

    @Autowired
    private PostScheduleService postScheduleService;

    @Test
    @DisplayName("관리자가 승인하지않은 상태에서 7일이 경과하면 자동으로 게시물이 삭제된다.")
    void 관리자가_승인하지않는_상태에_7일_경과시_자동으로_게시물이_삭제된다(){
        assertDoesNotThrow(() -> postScheduleService.postStateIsDeleteAnd7DaysOver());
    }
}
