package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum CheeringMessage {

    NONE(""),
    CHEERING1("힘내"),
    CHEERING2("같이하자"),
    CHEERING3("응원할게"),
    CHEERING4("화이팅"),
    CHEERING5("멋지다"),
    CHEERING6("최고야");

    private String title;

    public String getTitle() {
        return title;
    }
}
