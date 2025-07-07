package com.orangeschool.member.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateSchoolInfoDto {

    @Schema(description = "학교 명", example = "")
    private String schoolCode = "";
    @Schema(description = "학교 명", example = "")
    private String schoolName = "";
    @Schema(description = "학년", example = "")
    private String grade = "";
    @Schema(description = "반", example = "")
    private String schoolClass = "";
    @Schema(description = "번호", example = "")
    private String classNumber = "";
}