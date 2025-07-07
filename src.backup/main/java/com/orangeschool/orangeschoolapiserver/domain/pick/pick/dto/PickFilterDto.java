package com.orangeschool.orangeschoolapiserver.domain.pick.pick.dto;

import com.orangeschool.orangeschoolapiserver.common.dto.request.KeywordSearchDto;
import com.orangeschool.orangeschoolapiserver.common.enums.PickType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PickFilterDto extends KeywordSearchDto {

    @Schema(description = "구분", example = "NONE")
    private PickType pickType = PickType.NONE;

    @Schema(description = "처음 조회 여부", example = "false")
    private Boolean isFirst= false;
}
