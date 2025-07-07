package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ConfirmStatus {

    NONE(""),
    WAIT("승인 전"),
    REJECT("반려"),
    COMPLETE("승인"),
    DELETE("삭제");

    private String title;

    public String getTitle() {
        return title;
    }
}
