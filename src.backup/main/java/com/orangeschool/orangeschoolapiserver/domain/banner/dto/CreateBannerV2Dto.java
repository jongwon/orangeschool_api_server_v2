package com.orangeschool.orangeschoolapiserver.domain.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateBannerV2Dto {

    @Schema(description = "제목", example = "팝업 테스트1", required = true)
    @NotBlank
    private String title;
    @Schema(description = "시도 목록", example = "", required = true)
    @NotBlank
    private String regionCodeTag;
    @Schema(description = "시도 목록", example = "", required = true)
    @NotBlank
    private String regionNameTag;
    @Schema(description = "연결링크", example = "", required = false)
    private String link;
}
