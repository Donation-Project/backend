package com.donation.infrastructure.embed;

import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.PostFixtures.일반_게시물_게시글;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WriteTest {

    @Test
    @DisplayName("정상적으로 Write 객체 생성")
    void 정상적으로_Write_객체_생성(){
        //given & when & then
        assertDoesNotThrow(() -> 일반_게시물_게시글);
    }

    @Test
    @DisplayName("제목이 15글자를 초과할 경우 오류를 던진다")
    void 제목이_15글자를_초과할_경우_오류를_던진다(){
        //given
        String title = "a".repeat(16);
        String content = "b";

        //when & then
        assertThatThrownBy(() -> new Write(title, content))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("제목이 15자 이상일 수 없습니다.");
    }
}