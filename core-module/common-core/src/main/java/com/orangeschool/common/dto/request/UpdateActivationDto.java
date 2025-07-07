package com.orangeschool.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateActivationDto {

    @Schema(description = "활성: true, 비활성: false", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean activation = true;
}
