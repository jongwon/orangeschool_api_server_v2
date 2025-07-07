package com.orangeschool.orangeschoolapiserver.common.dto.response;

import lombok.Data;

@Data
public class AuthDto {

    private String accessToken;
    private Long id;
}
