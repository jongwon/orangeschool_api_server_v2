package com.orangeschool.orangeschoolapiserver.domain.commonMember.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UpdateSettingTimetableDto {

    @Schema(description = "회원 고유 아이디", example = "1", required = true)
    private Long commonMemberId = 0L;
    @Schema(description = "학교 시간", example = "")
    private String timetableEdit = "";
}