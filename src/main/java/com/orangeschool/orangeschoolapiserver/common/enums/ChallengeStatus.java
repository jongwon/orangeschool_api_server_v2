package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ChallengeStatus {

    NONE(""),
    PROGRESS("진행중"),
    END("완료");

    private String title;

}
