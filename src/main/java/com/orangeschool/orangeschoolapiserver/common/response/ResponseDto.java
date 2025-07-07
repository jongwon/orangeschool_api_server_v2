package com.orangeschool.orangeschoolapiserver.common.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDto {

    private String message;

    private Object data;

}
