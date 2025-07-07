package com.orangeschool.support.banner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdateBannerDto {

    @Schema(description = "링크", example = "")
    private String link = "";
}
