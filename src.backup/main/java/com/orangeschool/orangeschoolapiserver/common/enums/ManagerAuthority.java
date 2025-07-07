package com.orangeschool.orangeschoolapiserver.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ManagerAuthority {

    ROOT("ROOT"),
    ADMIN("ADMIN");

    private String title;

    public String getTitle() {
        return title;
    }
}
