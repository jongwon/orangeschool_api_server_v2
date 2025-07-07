package com.orangeschool.orangeschoolapiserver.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KeywordSearchDto {

    @Schema(description = "키워드", example = "")
    String keyword = "";
}
