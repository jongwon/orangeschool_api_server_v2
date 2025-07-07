package com.orangeschool.orangeschoolapiserver.domain.popup.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreatePopupDto {

    @Schema(description = "제목", example = "팝업 테스트1", required = true)
    @NotBlank
    private String title;
    @Schema(description = "게시링크", example = "팝업 링크1", required = false)
    private String link;
}
