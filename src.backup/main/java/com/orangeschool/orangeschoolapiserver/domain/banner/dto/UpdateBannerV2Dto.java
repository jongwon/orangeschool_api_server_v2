package com.orangeschool.orangeschoolapiserver.domain.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateBannerV2Dto {

    @Schema(description = "제목", example = "팝업 테스트1", required = true)
    @NotBlank
    private String title;
    @Schema(description = "시도 목록", example = "", required = true)
    @NotBlank
    private String regionCodeTag;
    @Schema(description = "시도 목록", example = "", required = true)
    @NotBlank
    private String regionNameTag;
    @Schema(description = "연결링크", example = "")
    private String link = "";
    @Schema(description = "활성: true, 비활성: false", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean activation = true;
}
