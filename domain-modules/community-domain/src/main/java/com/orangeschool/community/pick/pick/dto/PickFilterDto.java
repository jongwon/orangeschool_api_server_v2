package com.orangeschool.community.pick.pick.dto;

import com.orangeschool.common.dto.request.KeywordSearchDto;
import com.orangeschool.common.enums.PickType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
@EqualsAndHashCode(callSuper = false)@Data
public class PickFilterDto extends KeywordSearchDto {

    @Schema(description = "구분", example = "NONE")
    private PickType pickType = PickType.NONE;

    @Schema(description = "처음 조회 여부", example = "false")
    private Boolean isFirst= false;
}
