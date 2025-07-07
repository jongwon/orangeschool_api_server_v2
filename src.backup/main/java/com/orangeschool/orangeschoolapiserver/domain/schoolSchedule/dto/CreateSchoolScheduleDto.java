package com.orangeschool.orangeschoolapiserver.domain.schoolSchedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateSchoolScheduleDto {

    @Schema(description = "회원 고유 아이디", example = "1", required = true)
    private Long commonMemberId = 0L;
    @Schema(description = "키 값", example = "1-1-과목명", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String keyStringValue;
    @Schema(description = "중요일정 유무", example = "true")
    private Boolean isImportant = false;
    @Schema(description = "컬러", example = "#FA8431", required = true)
    @NotBlank
    private String color = "";
}
