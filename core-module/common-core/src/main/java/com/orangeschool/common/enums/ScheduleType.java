package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleType {

    NONE(""),
    ACADEMY("학원"),
    SCHOOL("학교"),
    CLASS("방과 후 교실"),
    VEHICLE("차량 승하차"),
    SCHEDULE("일정"),
    PAY("결제일"),
    ETC("기타 일상"),
    TUTORING("과외"),
    HOSPITAL("병원");

    private String title;

    public String getTitle() {
        return title;
    }
}
