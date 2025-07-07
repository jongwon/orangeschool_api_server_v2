package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleAlarmType {

    NONE(""),
    ZERO_MINUTES_AGO("이벤트 당시"),
    FIVE_MINUTES_AGO("5분전"),
    FIFTEEN_MINUTES_AGO("15분전"),
    THIRTY_MINUTES_AGO("30분전"),
    ONE_HOURS_AGO("1시간전"),
    TWO_HOURS_AGO("2시간전"),
    ONE_DAY_AGO("1일전");

    private String title;

    public String getTitle() {
        return title;
    }
}
