package com.orangeschool.orangeschoolapiserver.domain.cheering.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.CheeringMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CheeringRequestDto {

    @Schema(description = "응원 메세지", example = "CHEERING1", required = true)
    private CheeringMessage cheeringMessage = CheeringMessage.NONE;
}
