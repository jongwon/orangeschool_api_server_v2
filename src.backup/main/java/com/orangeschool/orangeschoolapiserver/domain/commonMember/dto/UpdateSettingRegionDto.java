package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateSettingRegionDto {

    @Schema(description = "시도 시군구 title 배열", example = "#서울 강남구#강원 강릉시#서울 강북구", required = false)
    private String regionNameTag = "";

    @Schema(description = "시군구 value 배열", example = "#11680#51150#11305", required = false)
    private String regionCodeTag = "";
}