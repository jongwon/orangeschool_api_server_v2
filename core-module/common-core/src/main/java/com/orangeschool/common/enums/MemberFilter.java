package com.orangeschool.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum     MemberFilter {

    NONE(""),
    NAME("이름"),
    EMAIL("이메일"),
    GENDER("성별"),
    BIRTH("생년월일"),
    PHONE_NUMBER("휴대폰번호"),
    ADDRESS("주소"),
    PARENT("등록 보호자");

    private String title;

    public String getTitle() {
        return title;
    }

}
