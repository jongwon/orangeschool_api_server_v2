package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CalendarCycle {

    NONE("", 0, 0),
    EVERY_DAY("매일", 1, 1),
    EVERY_WEEK("매주", 2, 1),
    TWO_WEEK("2주", 2, 2),
    THREE_WEEK("3주", 2, 3),
    FOUR_WEEK("4주", 2, 4),
    EVERY_MONTH("매월", 3, 1),
    EVERY_YEAR("매년", 4, 1);

    private String title;
    private int type; // 1: day, 2: week, 3: month, 4: year
    private int plusValue;

    public String getTitle() {
        return title;
    }
}
