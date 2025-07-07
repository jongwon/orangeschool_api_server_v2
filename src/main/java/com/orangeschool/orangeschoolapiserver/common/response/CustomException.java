package com.orangeschool.orangeschoolapiserver.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;
}
