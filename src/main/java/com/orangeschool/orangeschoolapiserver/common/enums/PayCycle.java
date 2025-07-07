package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PayCycle {

    NONE("", 0),
    ONE_MONTH("1개월", 1),
    TWO_MONTH("2개월", 2),
    THREE_MONTH("3개월", 3),
    FOUR_MONTH("4개월", 4),
    FIVE_MONTH("5개월", 5),
    SIX_MONTH("6개월", 6),
    SEVEN_MONTH("7개월", 7),
    EIGHT_MONTH("8개월", 8),
    NINE_MONTH("9개월", 9),
    TEN_MONTH("10개월", 10),
    ELEVEN_MONTH("11개월", 11),
    EVERY_YEAR("매년", 12);

    private String title;
    private int month;

    public String getTitle() {
        return title;
    }
}
