package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CycleType {

    NONE(""),
    DAY("요일로 설정"),
    PERIOD("주기로 설정"),;

    private String title;

    public String getTitle() {
        return title;
    }
}
