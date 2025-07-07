package com.orangeschool.orangeschoolapiserver.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FileDownloadDto {

    @Schema(description = "서버 파일 이름", example = "", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String serverFileName;
    @Schema(description = "파일 이름", example = "")
    private String originalFileName;
}
