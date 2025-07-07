package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum AlarmMemberType {
    NONE(""),
    ALL("전체"),
    PARENT("보호자"),
    CHILD("자녀");

    private String title;

    public String getTitle() {
        return title;
    }
}
