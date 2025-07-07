package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PickType {

    NONE(""),
    PARENT("보호자"),
    CHILD("자녀"),
    ALL("전체");

    private String title;

    public String getTitle() {
        return title;
    }
}
