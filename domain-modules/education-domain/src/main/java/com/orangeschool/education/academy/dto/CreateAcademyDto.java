package com.orangeschool.education.academy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateAcademyDto {

    @Schema(description = "학원명", example = "공지사항 제목1", required = true)
    @NotBlank
    private String academyName;
    @Schema(description = "주소", example = "서울특별시 ~~")
    private String address = "";
    @Schema(description = "상세주소", example = "상세주소 1")
    private String addressDetail = "";
}
