package com.orangeschool.orangeschoolapiserver.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class IdListDto {

    @Schema(description = "id 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    List<Long> idList;
}
