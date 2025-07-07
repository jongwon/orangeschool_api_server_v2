package com.orangeschool.schedule.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;


@Data
public class DeleteScheduleDto {

    @Schema(description = "수정 기준일", example = "2024-05-05", pattern = "yyyy-MM-dd", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate deleteStandardDate;
}