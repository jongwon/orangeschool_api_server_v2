package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum AlarmType {

    NONE(""),
    AD("광고성 알림"),
    SERVICE("서비스 알림"),
    SYSTEM("시스템 알림"),
    SCHEDULE("일정 알림"),;

    private String title;

    public String getTitle() {
        return title;
    }
}
