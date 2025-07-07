package com.orangeschool.orangeschoolapiserver.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangeschool.orangeschoolapiserver.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateSleepDto {

    @Schema(description = "기상 시간", example = "08:00:00", pattern = "HH:mm:ss", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime wakeTime;
    @Schema(description = "취침 시간", example = "23:00:00", pattern = "HH:mm:ss", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss", timezone = "Asia/Seoul")
    private LocalTime sleepTime;
}