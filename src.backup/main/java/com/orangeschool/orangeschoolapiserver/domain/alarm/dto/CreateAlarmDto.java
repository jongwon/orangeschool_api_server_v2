package com.orangeschool.orangeschoolapiserver.domain.alarm.dto;

import com.orangeschool.orangeschoolapiserver.common.enums.AlarmMemberType;
import com.orangeschool.orangeschoolapiserver.common.enums.AlarmType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateAlarmDto {

    @Schema(description = "제목", example = "광고알람", required = true)
    @NotBlank
    private String title;
    @Schema(description = "본문", example = "테스트 알림1")
    @NotBlank
    private String content = "";
    @Schema(description = "알림 종류", example = "SERVICE", required = true)
    private AlarmType alarmType = AlarmType.NONE;
    @Schema(description = "알림 회원 종류", example = "ALL", required = true)
    private AlarmMemberType alarmMemberType = AlarmMemberType.NONE;
}
