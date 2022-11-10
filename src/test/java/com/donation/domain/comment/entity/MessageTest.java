package com.donation.domain.comment.entity;

import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MessageTest {

    @Test
    @DisplayName("빈 내용으로 댓글을 작성할 경우 예외를 던진다.")
    void 빈_내용으로_댓글을_작성할_경우_예외를_던진다() {
        //given & when & then
        assertThatThrownBy(() -> new Message(""))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("댓글의 내용이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글의 내용이 Null일 경우 예외를 던진다.")
    void 댓글의_내용이_Null일_경우_예외를_던진다() {
        //given & when & then
        assertThatThrownBy(() -> new Message(null))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("댓글의 내용이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("댓글이 100글자를 초과할 경우 예외를 던진다.")
    void 댓글이_100글자를_초과할_경우_예외를_던진다(){
        //given
        String massage = "12345678910".repeat(10);

        //when & then
        assertThatThrownBy(() -> new Message(massage))
                .isInstanceOf(DonationInvalidateException.class)
                .hasMessage("댓글의 길이가 제한을 초과합니다.");
    }

    @ParameterizedTest
    @DisplayName("댓글 내용 작성을 성공한다")
    @ValueSource(strings = {"댓글1", "댓글2 : 테스트 코드 작성입니다.", "안녕하세요"})
    void 댓글_내용_작성을_성공한다(String message){
        assertDoesNotThrow(() -> new Message(message));
    }
}
