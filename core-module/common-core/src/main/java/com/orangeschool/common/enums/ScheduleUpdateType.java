package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ScheduleUpdateType {

    NONE(""),
    CURRENT_ALL("이번부터 수정"),
    ALL("모든 일정 수정");

    private String title;

    public String getTitle() {
        return title;
    }
}
