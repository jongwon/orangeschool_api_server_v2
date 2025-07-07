package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleRequestType {

    NONE(""),
    CREATE("등록"),
    UPDATE("수정");

    private String title;

    public String getTitle() {
        return title;
    }
}
