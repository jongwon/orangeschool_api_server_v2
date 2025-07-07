package com.orangeschool.schedule.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orangeschool.common.enums.MemberType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateDayMessageDto {

    @Schema(description = "일정 날짜", example = "2023-05-01", pattern = "yyyy-MM-dd", type = "string", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dayDate;
    @Schema(description = "메시지 남기는 사람 유형", example = "")
    MemberType memberType = MemberType.NONE;
    @Schema(description = "부모님 메모", example = "부모 메시지")
    private String parentMessage = "";
    @Schema(description = "부모님 메모", example = "아이 메시지")
    private String childMessage = "";
}