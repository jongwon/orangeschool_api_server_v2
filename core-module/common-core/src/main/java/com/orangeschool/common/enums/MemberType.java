package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum MemberType {

    NONE(""),
    PARENT("보호자"),
    CHILD("자녀");

    private String title;

    public String getTitle() {
        return title;
    }
}
