package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum LeaveType {
    NO_WANTED("원하는 서비스가 없어요"),
    USE_DIFFICULT("오렌지스쿨을 사용하기가 어려워요."),
    BAD_EX("오렌지스쿨에서 불쾌한 경험을 했어요."),
    NEW_ACCOUNT("새로운 계정을 만들고 싶어요."),
    ETC("기타");

    private String title;

    public String getTitle() {
        return title;
    }

}
