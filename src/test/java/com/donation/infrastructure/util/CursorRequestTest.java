package com.donation.infrastructure.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CursorRequestTest {

    @Test
    @DisplayName("Key는 null을 허용하고 size는 null일 경우 20으로 생성된다.")
    void Key는_null을_허용하고_size는_null일_경우_20으로_생성된다(){
        //given
        Long key = null;
        Integer size = null;

        //when
        CursorRequest actual = new CursorRequest(key, size);

        //then
        Assertions.assertAll(() -> {
            assertEquals(actual.getKey(), null);
            assertEquals(actual.getSize(), 20);
        });
    }

    @Test
    @DisplayName("Key가 null일 경우 hasKey는 false를 반환한다.")
    void Key가_null일_경우_hasKey는_false를_반환한다(){
        //given
        Long key = null;
        Integer size = null;

        //when
        CursorRequest actual = new CursorRequest(key, size);

        //then
        assertThat(actual.hasKey()).isFalse();
    }
}
