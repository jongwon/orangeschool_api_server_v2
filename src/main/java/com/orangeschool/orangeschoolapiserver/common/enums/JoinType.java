package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum JoinType {
    NORMAL("일반"),
    KAKAO("카카오"),
    GOOGLE("구글"),
    APPLE("애플"),
    NAVER("네이버");

    private String title;

    public String getTitle() {
        return title;
    }

}
