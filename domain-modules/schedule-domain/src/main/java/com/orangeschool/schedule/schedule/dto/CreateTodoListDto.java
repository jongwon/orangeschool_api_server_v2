package com.orangeschool.schedule.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTodoListDto {

    @Schema(description = "일정 날짜", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dayDate;
    @Schema(description = "오늘의 할일", example = "[]")
    private String todoList = "";
}