package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PayAlarmType {

    NONE(""),
    YESTERDAY_9AM("전일 오전 9시"),
    TODAY_9AM("당일 오전 9시");

    private String title;

    public String getTitle() {
        return title;
    }
}
